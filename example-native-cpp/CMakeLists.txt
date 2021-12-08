cmake_minimum_required(VERSION 3.10)

project(brac-kt-native-cpp)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED True)

add_executable(brac-kt-native-cpp src/app.cpp)

# Linux:
# 1. Run: ./gradlew :brac-kt-ibmq-provider:linkDebugSharedLinuxX64
#target_link_libraries(brac-kt-native-cpp ${CMAKE_SOURCE_DIR}/../brac-kt-ibmq-provider/build/bin/linuxX64/debugShared/libbrac_kt_ibmq_provider.so)

# Windows:
# 1. Run: ./gradlew :brac-kt-ibmq-provider:linkDebugSharedMingwX64
# 2. Run: lib /def:brac_kt_ibmq_provider.def /out:brac_kt_ibmq_provider.lib
# 3. Copy brac_kt_ibmq_provider.dll to C:\Windows\System32\
# 4. Copy your libcurl (libcurl.dll, libcurl-4.dll, etc.) as C:\Windows\System32\libcurl-4.dll
target_link_libraries(brac-kt-native-cpp ${CMAKE_SOURCE_DIR}/../brac-kt-ibmq-provider/build/bin/mingwX64/debugShared/brac_kt_ibmq_provider.lib)
