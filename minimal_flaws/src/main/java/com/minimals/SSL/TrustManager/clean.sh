#!/bin/bash

cd 1BadSSL_Naive
./clean.sh
cd ..
cd 2BareBone_X509TrustManager
./clean.sh
cd ..
cd 3BareBone_X509TrustManagerCanNotBypass
./clean.sh
cd ..
cd 4BareBone_X509ExtendedTrustManagerCanBypass
./clean.sh
cd ..
cd 5BareBone_X509TrustManagerCanBypass
./clean.sh
cd ..
cd 6ITrustManagerUser
./clean.sh
cd ..
cd 7BareBone_x509TrustManagerCanNotBypass
./clean.sh
cd ..
cd 8BareboneTrustManagerConditional
./clean.sh
cd ..