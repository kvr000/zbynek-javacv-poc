# JavaCv - broken index demo

## Build

```
mvn package
```

## Run

```
java -jar target/javacv-broken-index.jar some-video.mp4 /tmp/a.mp4 120
ffplay a.mp4
```

## Result

Video won't seek after 1:28 .

Tested on:

OS:
```
Linux ratteburg 6.8.0-49-generic #49-Ubuntu SMP PREEMPT_DYNAMIC Mon Nov  4 02:06:24 UTC 2024 x86_64 x86_64 x86_64 GNU/Linux
```

Java:
```
openjdk 22-ea 2024-03-19
OpenJDK Runtime Environment (build 22-ea+16-Ubuntu-1)
OpenJDK 64-Bit Server VM (build 22-ea+16-Ubuntu-1, mixed mode, sharing)
```

Ffplay:
```
Input #0, mov,mp4,m4a,3gp,3g2,mj2, from '/tmp/a.mp4':    0B f=0/0   
  Metadata:
    major_brand     : isom
    minor_version   : 512
    compatible_brands: isomiso2avc1mp41
    encoder         : Lavf61.7.100
  Duration: 00:01:59.98, start: 0.000000, bitrate: 16989 kb/s
  Stream #0:0[0x1](eng): Video: h264 (Constrained Baseline) (avc1 / 0x31637661), yuv420p(progressive), 1920x1080, 16883 kb/s, 25 fps, 25 tbr, 12800 tbn (default)
    Metadata:
      handler_name    : Video Media Handler
      vendor_id       : [0][0][0][0]
      encoder         : AVC Coding
  Stream #0:1[0x2](eng): Audio: aac (LC) (mp4a / 0x6134706D), 44100 Hz, mono, fltp, 131 kb/s (default)
    Metadata:
      handler_name    : Sound Media Handler
      vendor_id       : [0][0][0][0]
/tmp/a.mp4: error while seeking   19KB vq= 2132KB sq=    0B f=0/0   
/tmp/a.mp4: error while seeking   11KB vq= 1145KB sq=    0B f=0/0   
/tmp/a.mp4: error while seeking    5KB vq=  317KB sq=    0B f=0/0   
/tmp/a.mp4: error while seeking    1KB vq=    0KB sq=    0B f=0/0   
  89.47 A-V:  0.008 fd=   0 aq=    0KB vq=    0KB sq=    0B f=0/0   
```
