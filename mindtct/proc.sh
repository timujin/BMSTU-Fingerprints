#!/bin/bash
for file in ./*
do
	/cygdrive/c/Fingerprint/bin/mindtct.exe $file $file
done
