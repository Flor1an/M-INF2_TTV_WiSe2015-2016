#!/bin/sh

#kill processes
killall batmand
killall oslrd

#configure wifi
iw wlan0 ibss leave
ifconfig wlan0 down
iwconfig wlan0 txpower 10
ifconfig wlan0 up
# set params       -SSID- -FREQ- -BSSID-----------
iw wlan0 ibss join oslr   2462   02:aa:bb:cc:dd:02
ifconfig wlan0 10.0.0.14 netmask 255.255.255.0

#firewall deactivations
/etc/init.d/firewall stop
iptables -I FORWARD -s 10.0.0.0/24 -j ACCEPT
iptables -I FORWARD -d 10.0.0.0/24 -j ACCEPT
iptables -t nat -A POSTROUTING -s 192.168.14.0/24 -j MASQUERADE

#start process
olsrd -f /etc/olsrd.conf

#add default gateway
route add default gw 10.0.0.1


