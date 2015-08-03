LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := YongBaoDevice
LOCAL_SRC_FILES := com_jinwang_yongbao_device_Device.c
LOCAL_ALLOW_UNDEFINED_SYMBOLS := true
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)