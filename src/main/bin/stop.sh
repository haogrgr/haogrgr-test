#!/bin/bash
ls -l kepler_* | awk '{print $9}' | awk -F'[.|_]' '{print $2}' | xargs kill