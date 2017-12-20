#include <jni.h>
#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

extern "C" {


JNIEXPORT jstring

JNICALL
Java_com_example_user_cmake_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jstring
Java_com_example_user_cmake_MainActivity_imageTest(JNIEnv *env, jobject, jlong addrImage)
{
    cv::Mat* inputImage_p = (cv::Mat*)addrImage;
    cv::cvtColor(*inputImage_p, *inputImage_p, CV_BGR2GRAY);
    std::string hello2 = "Hello from imageTest";
    return env->NewStringUTF(hello2.c_str());
}

}