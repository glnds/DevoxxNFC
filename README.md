## About
Application for NFC tag scanning

## Platform

The target platform to run this application is the **Raspberry Pi**. Certain functionality (linked to GPIO) will only work on that platform.

## Dependencies
The following applications should be installed on the Raspberry Pi:

### Pi4J
http://pi4j.com/<br />

#### Installation
    # wget http://pi4j.googlecode.com/files/pi4j-0.0.5.deb
    # sudo dpkg -i pi4j-0.0.5.deb

Either copy the lib files to your jre/lib/ext directory or add them to your classpath when running the application.

### RabbitMQ
http://www.rabbitmq.com/

#### Installation

Add the following line to your /etc/apt/sources.list:
<pre><code># deb http://www.rabbitmq.com/debian/ testing main</code></pre>
    # wget http://www.rabbitmq.com/rabbitmq-signing-key-public.asc
    # sudo apt-key add rabbitmq-signing-key-public.asc
    # apt-get update.
    # sudo apt-get install rabbitmq-server
    # sudo rabbitmq-plugins enable rabbitmq_management
	# sudo /etc/init.d/rabbitmq-server restart 

Once RabbitMQ is installed you can accesses the management interface by navigating to your PI's IP address and adding :55672/#/.

## Run ##
usage: DevoxxNFC<br />
 -d,--debug             enter debug mode<br />
 -m,--messaging         enable messaging<br />
 -p,--platform <name>   specify then platform<br />
 -r,--room <number>     use number to specify the room
 
## Architectuur

## protocol

## References
http://teaandterminals.blogspot.co.uk/2012/11/rabbit-pi.html

https://code.google.com/p/boblight/
https://github.com/adammhaile/RPi-LPD8806
https://github.com/Sh4d/LPD8806
http://learn.adafruit.com/light-painting-with-raspberry-pi/overview

