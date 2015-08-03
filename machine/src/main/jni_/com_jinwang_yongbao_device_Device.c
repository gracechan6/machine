#include "com_jinwang_yongbao_device_Device.h"

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <errno.h>
#include <linux/unistd.h>
#include <pthread.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/ioctl.h>
#include <sys/select.h>
#include <sys/time.h>
#include <linux/input.h>
#include <android/log.h>
#include <math.h>

#define LOG_TAG "TEST_SERIAL"
#undef  LOG
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

static int uart_fd;

//配置串口参数
int set_opt(int fd, int nSpeed, int nBits, char nEvent, int nStop) {
	struct termios newtio, oldtio;
	if (tcgetattr(fd, &oldtio) != 0) {
		perror("SetupSerial 1");
		return -1;
	}
	bzero(&newtio, sizeof(newtio));
	newtio.c_cflag &= ~CSTOPB;
	        newtio.c_cflag &= ~CSIZE;
	        newtio.c_cflag |= (CLOCAL | CREAD);
	        newtio.c_cflag &= ~CRTSCTS;

	        /* set no software stream control */
	        newtio.c_iflag &= ~(IXON | INLCR | ICRNL | IGNCR | IUCLC);
	        /* set output mode with no define*/
	        newtio.c_oflag &= ~OPOST;
	        /* set input mode with non-format */
	        newtio.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);
	        newtio.c_iflag |= IGNBRK|IGNPAR; //for 0xd,0x11,0x13

	switch (nBits) {
	case 7:
		newtio.c_cflag |= CS7;
		break;
	case 8:
		newtio.c_cflag |= CS8;
		break;
	}

	switch (nEvent) {
	case 'O':
		newtio.c_cflag |= PARENB;
		newtio.c_cflag |= PARODD;
		newtio.c_iflag |= (INPCK | ISTRIP);
		break;
	case 'E':
		newtio.c_iflag |= (INPCK | ISTRIP);
		newtio.c_cflag |= PARENB;
		newtio.c_cflag &= ~PARODD;
		break;
	case 'N':
		newtio.c_cflag &= ~PARENB;
		break;
	}

	switch (nSpeed) {
	case 2400:
		cfsetispeed(&newtio, B2400);
		cfsetospeed(&newtio, B2400);
		break;
	case 4800:
		cfsetispeed(&newtio, B4800);
		cfsetospeed(&newtio, B4800);
		break;
	case 9600:
		cfsetispeed(&newtio, B9600);
		cfsetospeed(&newtio, B9600);
		break;
	case 38400:
		cfsetispeed(&newtio, B38400);
		cfsetospeed(&newtio, B38400);
		break;
	case 115200:
		cfsetispeed(&newtio, B115200);
		cfsetospeed(&newtio, B115200);
		break;
	case 460800:
		cfsetispeed(&newtio, B460800);
		cfsetospeed(&newtio, B460800);
		break;
	default:
		cfsetispeed(&newtio, B9600);
		cfsetospeed(&newtio, B9600);
		break;
	}
	if (nStop == 1)
		newtio.c_cflag &= ~CSTOPB;
	else if (nStop == 2)
		newtio.c_cflag |= CSTOPB;
	newtio.c_cc[VTIME] = 0;
	newtio.c_cc[VMIN] = 0;
	tcflush(fd, TCIFLUSH);
	if ((tcsetattr(fd, TCSANOW, &newtio)) != 0) {
		perror("com set error");
		return -1;
	}
	return 0;
}

/*
wr: '0', 设置rs485模块为写模式
    '1', 设置rs485模块为读模式, 注意是字符串
*/
int control_rs485(char wr){
	int fd = open("/sys/class/io_control/rs485_con", O_RDWR | O_NOCTTY);
	if (fd == -1) {
		return 2;
	}
	write(fd, &wr, 1);
	close(fd);
}

/**
 * 初始化设备
 * return -1 初始化错误 0 初始化成功
 */
int Java_com_jinwang_yongbao_device_Device_uartInit(JNIEnv* env, jobject thiz) {
	//打开串口
	uart_fd = open("/dev/ttyS3", O_RDWR);
		if (uart_fd == -1) {
			return -1;
		}
		//配置串口
		int nset = set_opt(uart_fd, 9600, 8, 'N', 1);
		if (nset == -1) {
			return -1;
		}

	return 0;
}

/**
 * 关闭设备
 */
int Java_com_jinwang_yongbao_device_Device_uartDestroy(JNIEnv* env, jobject thiz) {
	close(uart_fd);

	return 0;
}

/**
 * 打开箱格
 * cardID：板子标号
 * doorID：门号
 *
 * return 0表示操作成功，-1代表失败
 */
int Java_com_jinwang_yongbao_device_Device_openGrid(JNIEnv* env, jobject thiz, jint cardID,
		jint doorID, jintArray info) {
	int* buf;
		int i, ret=-1, len,j;
		jboolean isCopy;

	//协议五位  命令：0x8A   板地址：0X01-0XC8    锁地址：0X01—18    状态：0X11    校检码：前面几位异或
	char xwdata[5] = { 0X8A, (char) cardID, (char) doorID, 0x11 };
	char xrdata[5] = { 0 };
	char xrdata_tmp[50] = {0};
	int start;
	xwdata[4] = (xwdata[0] ^ xwdata[1] ^ xwdata[2] ^ xwdata[3]) & 0xff;
	LOGD("Open cardId %d door %d\n",cardID,doorID);
	LOGD("open send:0x%x:0x%x:0x%x:0x%x:0x%x\n",xwdata[0],xwdata[1],xwdata[2],xwdata[3],xwdata[4]);

	tcflush(uart_fd,   TCIOFLUSH);
	control_rs485('0');
		write(uart_fd, xwdata, 5);
		usleep(10000); //必须等待一段时间，rs485才会把数据发送出去
		control_rs485('1');
		usleep(1000000);
		read(uart_fd,xrdata,5);
		/*
		memset(xrdata_tmp,0,sizeof(xrdata));

		start = 0;
		for(i=0; i<50; i++) {
		    read(uart_fd, xrdata_tmp+i, 1);
			//LOGD("%d:0x%x\n",i,xrdata_tmp[i]);
			if((xrdata_tmp[i] == 0x8a) && (start == 0)){
				start = i;
			}
			usleep(10000);
		}

		LOGD("start %d\n",start);
		for(i=0; i<5; i++)
			xrdata[i] = xrdata_tmp[start+i];
			*/

		LOGD("recv:0x%x:0x%x:0x%x:0x%x:0x%x\n",xrdata[0],xrdata[1],xrdata[2],xrdata[3],xrdata[4]);

		buf = (int*)(*env)->GetIntArrayElements(env,info,&isCopy);
		for(i=0;i<5;i++)
		{
			buf[i] = xrdata[i];
		}

		if(isCopy)
		{
			(*env)->ReleaseIntArrayElements(env,info, (jint*)buf, JNI_ABORT);
		}

		return 0;
}

/**
 * 获取锁控板地址
 *
 */
int Java_com_jinwang_yongbao_device_Device_getBoardAddress(JNIEnv* env, jobject thiz, jintArray info) {
	int i,ret,boardIndex,j;
	int* buf;
	jboolean isCopy;
	buf = (int*)(*env)->GetIntArrayElements(env,info,&isCopy);
	//协议五位  命令：0x81  0x01  板地址：0X00    状态：0X99    校检码：前面几位异或 0X19
	char xwdata[5] = { 0X81, 0X01, 0x00, 0x99 , 0x19};
	char xrdata[5] = { 0 };
	tcflush(uart_fd,   TCIOFLUSH);
	control_rs485('0');
	boardIndex = 0;
	//遍历核心板，询问是否有回应
	for(i=0; i<16; i++) {

		control_rs485('0');
		usleep(5000);
		xwdata[1] = i;
		xwdata[4] = (xwdata[0] ^ xwdata[1] ^ xwdata[2] ^ xwdata[3]) & 0xff;
		LOGD("send:0x%x:0x%x:0x%x:0x%x:0x%x\n",xwdata[0],xwdata[1],xwdata[2],xwdata[3],xwdata[4]);
		write(uart_fd, xwdata, 5);
		usleep(5000); //必须等待一段时间，rs485才会把数据发送出去
		control_rs485('1');
		usleep(500000);//等待0.5秒
		memset(xrdata,0,sizeof(xrdata));
		ret = read(uart_fd, xrdata, 5);
		LOGD("recv:ret %d:%d 0x%x:0x%x:0x%x:0x%x:0x%x\n",
							ret, i,xrdata[0],xrdata[1],xrdata[2],xrdata[3],xrdata[4]);
		if(ret != 0 && (xrdata[0] == 0x80) &&
				((xrdata[0]^xrdata[1]^xrdata[2]^xrdata[3]^xrdata[4]) == 0x0)) {
			buf[boardIndex] = xrdata[1];
			boardIndex++;
		}
		usleep(50000);
	}
	LOGD("get address done\n");

	if(isCopy)
              (*env)->ReleaseIntArrayElements(env, info, (jint*)buf, JNI_ABORT);
	return boardIndex;
}

/*
 * 获取协议ID*/
int Java_com_jinwang_yongbao_device_Device_getProtocalID(JNIEnv* env, jobject thiz, jintArray info) {
	int i,ret,boardIndex,j;
	int* buf;
	jboolean isCopy;
	buf = (int*)(*env)->GetIntArrayElements(env,info,&isCopy);
	//协议五位  命令：0X91, 0Xfe, 0xfe, 0xfe , 0x6f
	char xwdata[5] = { 0X91, 0Xfe, 0xfe, 0xfe , 0x6f};
	char xrdata[5] = { 0 };
	tcflush(uart_fd,   TCIOFLUSH);
	control_rs485('0');
		write(uart_fd, xwdata, 5);
		usleep(5000); //必须等待一段时间，rs485才会把数据发送出去
		control_rs485('1');
		usleep(500000);//等待0.5秒
		i=0;
		ret = read(uart_fd, xrdata, 5);

		LOGD("recv:ret %d, 0x%x:0x%x:0x%x:0x%x:0x%x\n",
				ret,xrdata[0],xrdata[1],xrdata[2],xrdata[3],xrdata[4]);

		for(i=0;i<5;i++)
				buf[i] = xrdata[i];
		if(isCopy)
	              (*env)->ReleaseIntArrayElements(env,info, (jint*)buf, JNI_ABORT);
		return 0;


}
/**
 * 获取锁的状态
 *
 */
int Java_com_jinwang_yongbao_device_Device_getDoorState(JNIEnv* env, jobject thiz, jint boardID, jint doorID, jintArray info) {

	int i, len,ret;
	//协议五位  命令：0x80 板地址：0X01-0xC8   锁地址:0x00-0x18  命令:0x33    校检码：前面几位异或
	char xwdata[5] = { 0X80, (char)boardID, (char)doorID, 0x33};
	char xrdata[7] = { 0 };
	if(doorID == 0)
		len = 7;
	else
		len = 5;

	xwdata[4] = (xwdata[0] ^ xwdata[1] ^ xwdata[2] ^ xwdata[3]) & 0xff;
	LOGD("get state send:0x%x:0x%x:0x%x:0x%x:0x%x\n",xwdata[0],xwdata[1],xwdata[2],xwdata[3],xwdata[4]);

	tcflush(uart_fd,   TCIOFLUSH);
	control_rs485('0');
	write(uart_fd, xwdata, 5);
	usleep(5000); //必须等待一段时间，rs485才会把数据发送出去
	control_rs485('1');
	usleep(500000);//等待0.5秒
	i=0;
	ret = read(uart_fd, xrdata, len);

	LOGD("recv:ret %d, 0x%x:0x%x:0x%x:0x%x:0x%x:0x%x:0x%x\n",
			ret,xrdata[0],xrdata[1],xrdata[2],xrdata[3],xrdata[4],xrdata[5],xrdata[6]);

	int* buf;

	jboolean isCopy;
	buf = (int*)(*env)->GetIntArrayElements(env,info,&isCopy);

	for(i=0;i<len;i++)
			buf[i] = xrdata[i];
	if(isCopy)
              (*env)->ReleaseIntArrayElements(env,info, (jint*)buf, JNI_ABORT);
	return 0;
}

