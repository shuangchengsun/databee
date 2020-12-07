#!/bin/zsh
docker build -t sunshuangcheng/databee .
docker run --name dataBeeContainer \
-d -p 8081:8081 \
-v /Users/sunshuangcheng/Docker/dataBee/logs:/logs \
-v /Users/sunshuangcheng/Docker/dataBee/tmp:/tmp \
sunshuangcheng/databee
