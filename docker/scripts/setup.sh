#!/bin/bash

set -e

# Script for installing all dependencies and setup

OS=$(uname -s)
ROOT=../

pwd
cd $ROOT

apt-get -qq update 

#  Curl, OpenJDK22
apt-get  -qqy install --no-install-recommends \
    curl \
    openjdk-17-jre

# Lein
curl https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein > lein
mv lein /usr/local/bin/lein
chmod a+x /usr/local/bin/lein
