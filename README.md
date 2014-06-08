## About
Application offering the possibility to vote by using NFC tags.

## Platform

The target platform for this application is the **Raspberry Pi**. Certain functionality (linked to GPIO) will only work on that platform.

## Installation

### Arch linux
The following applications should be installed on the Raspberry Pi:

#### Pi4j
- Download the Arch build [here](https://github.com/glnds/pi4j-arch/releases/tag/arch-release%2F0.0.5).
- Copy the Pi4J library to your Raspberry pi:

		scp pi4j-0.0.5.tar.gz pi@rpi.local:~/
- Extract the archive:

		tar -xvzf pi4j-0.0.5.tar.gz
- Install the libraries:

		sudo mkdir /opt/pi4j
		sudo mv ~/pi4j-0.0.5/lib/ /opt/pi4j/
		sudo mv ~/pi4j-0.0.5/examples/ /opt/pi4j/

#### Disable pn533 and nfc driver in kernel

	echo "install nfc /bin/false" >> /etc/modprobe.d/blacklist.conf
	echo "install pn533 /bin/false" >> /etc/modprobe.d/blacklist.conf

#### Install Pcsclite

	sudo pacman -S pcsclite ccid pcsc-tools
	
	sudo systemctl start  pcscd.service
  	sudo systemctl enable  pcscd.service


#### Install DevoxxNFC

- install the depencies
	
		scp LedStrip-1.0.jar pi@hostname:~/nfc/lib  
		scp amqp-client-3.1.4.jar pi@hostname:~/nfc/lib    
		scp commons-cli-1.2.jar pi@hostname:~/nfc/lib    
		scp commons-validator-1.4.0.jar pi@hostname:~/nfc/lib  

## Run

	 sudo java -cp lib/LedStrip-1.0.jar:lib/amqp-client-3.1.4.jar:lib/commons-cli-1.2.jar:lib/commons-validator-1.4.0.jar:DevoxxNFC.jar:.:classes:/opt/pi4j/lib/'*' be.pixxis.devoxx.NFCScanner -r 1 -s 172.0.0.1

	




### Pi4J
http://pi4j.com/<br />

#### Installation
    # wget http://pi4j.googlecode.com/files/pi4j-0.0.5.deb
    # sudo dpkg -i pi4j-0.0.5.deb



### RabbitMQ
http://www.rabbitmq.com/





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

https://code.google.com/p/boblight/<br />
https://github.com/adammhaile/RPi-LPD8806<br />
https://github.com/Sh4d/LPD8806<br />
http://learn.adafruit.com/light-painting-with-raspberry-pi/overview

