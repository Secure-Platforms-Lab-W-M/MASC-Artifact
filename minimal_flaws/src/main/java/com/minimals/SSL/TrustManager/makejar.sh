#!/bin/bash

cd 1BadSSL_Naive
./makejar.sh
cd ..
cd 2BareBone_X509TrustManager
./makejar.sh
cd ..
cd 3BareBone_X509TrustManagerCanNotBypass
./makejar.sh
cd ..
cd 4BareBone_X509ExtendedTrustManagerCanBypass
./makejar.sh
cd ..
cd 5BareBone_X509TrustManagerCanBypass
./makejar.sh
cd ..
cd 6ITrustManagerUser
./makejar.sh
cd ..
cd 7BareBone_x509TrustManagerCanNotBypass
./makejar.sh
cd ..
cd 8BareboneTrustManagerConditional
./makejar.sh
cd ..
