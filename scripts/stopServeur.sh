ps -ef | grep java | grep code-story | grep -v grep | while read a b c ; do kill -9 $b; done
