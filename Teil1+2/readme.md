TTVP 1+2 (Mesh Routing)
===================
**Technik & Technologie vernetzter Systeme**

![alt text](http://abload.de/img/320px-official_b.a.t.s1qyp.png) ![alt text](http://abload.de/img/olsr-logoqzpzb.jpg)

Mashed routing network using the B.A.T.M.A.N. vs. the OLSR protocol.

> **Note:** 
> - This document contains parts of the official lab document!<sup>[1](#myfootnote1)</sup>
> - settings configured for device 14

## <i class="icon-desktop"></i> DESKTOP:
The following steps need to get performed on the desktop device.

#### <i class="icon-desktop"></i> 1) configure nic:<sup>[2](#myfootnote2)</sup>
```
sudo /sbin/ifconfig w2p1 192.168.14.2 netmask 255.255.255.0
```

####<i class="icon-desktop"></i> 2) copy startup scripts:
```
scp batman.sh root@192.168.14.1:/~/
scp olsrd.sh root@192.168.14.1:/~/
```

####<i class="icon-desktop"></i> 3) copy olsrd confic file:
```
scp olsrd.conf root@192.168.14.1:/etc/
```

####<i class="icon-desktop"></i> 4) copy installation files:
```
scp kmod-tun_3.18.20-1_ar71xx.ipk libpcap_1.5.3-1_ar71xx.ipk libpthread_0.9.33.2-1_ar71xx.ipk uclibcxx_0.2.4-1_ar71xx.ipk batmand_r1439-2_ar71xx.ipk olsrd_0.9.0.2-3_ar71xx.ipk olsrd-mod-dyn-gw_0.9.0.2-3_ar71xx.ipk olsrd-mod-httpinfo_0.9.0.2-3_ar71xx.ipk iperf_2.0.5-1_ar71xx.ipk tcpdump_4.5.1-4_ar71xx.ipk wireless-tools_29-5_ar71xx.ipk root@192.168.14.1:/tmp/
```

####<i class="icon-desktop"></i> 5) connect via ssh:
```
ssh root@192.168.14.1 #Password = admin
```
---

## <i class="icon-signal"></i> ROUTER
these steps needs to get performed on the router device 

####<i class="icon-signal"></i> 1) install packages:
> **Note 1:** not on even device numbers as they are installed allredy

> **Note 2:** due to dependencys may need to run more than once

```
opkg install /tmp/kmod-tun_3.18.20-1_ar71xx.ipk
opkg install /tmp/libpcap_1.5.3-1_ar71xx.ipk
opkg install /tmp/libpthread_0.9.33.2-1_ar71xx.ipk
opkg install /tmp/uclibcxx_0.2.4-1_ar71xx.ipk

opkg install /tmp/batmand_r1439-2_ar71xx.ipk
opkg install /tmp/olsrd_0.9.0.2-3_ar71xx.ipk
opkg install /tmp/olsrd-mod-dyn-gw_0.9.0.2-3_ar71xx.ipk
opkg install /tmp/olsrd-mod-httpinfo_0.9.0.2-3_ar71xx.ipk
opkg install /tmp/iperf_2.0.5-1_ar71xx.ipk
opkg install /tmp/tcpdump_4.5.1-4_ar71xx.ipk
opkg install /tmp/wireless-tools_29-5_ar71xx.ipk
```

####<i class="icon-signal"></i> 2) modify `/etc/resolv.conf` to:<sup>[2](#myfootnote2)</sup> 
```bash
nameserver 141.22.192.100
nameserver 141.22.192.101
```

####<i class="icon-signal"></i> 3) modify `/etc/config/wireless` to:<sup>[2](#myfootnote2)</sup> 
```
#TODO: TBD
config wifi-iface
option device radio0
option mode adhoc
option encryption none
```

####<i class="icon-signal"></i> 4) activate wifi module:<sup>[2](#myfootnote2)</sup>
```
wifi
```

####<i class="icon-signal"></i> 5) change/disable autostart:
```
mv /etc/rc.d/S65olsr /~/S65olsr 
mv /etc/rc.d/S65olsrd /~/S65olsrd 
mv /etc/rc.d/S90batmand /~/S90batmand
```


####<i class="icon-signal"></i> 6) start the protocol:
```sh /~/batman.sh```
or
```sh /~/olsrd.sh```

---
##Status

### B.A.T.M.A.N.
```batmand -c -d 1```

###OLSR

- http://192.168.14.1:8080

---

##Tracing
Traceing the route to the B.A.T.M.A.N. gateway:
```traceroute 10.0.0.1```

Traceing the route to the OLSR gateway:
```traceroute 10.0.0.2```


---

##Mesarurement

<i class="icon-hourglass"></i> *TBN*




<a name="myfootnote1">1</a>: http://inet.cpt.haw-hamburg.de/teaching/ws-2015-16/technik-und-technologie-vernetzter-systeme/lab1.
  
<a name="myfootnote2">2</a>: Modified code snippeds taken from the official lab manual:  http://inet.cpt.haw-hamburg.de/teaching/ws-2015-16/technik-und-technologie-vernetzter-systeme/lab1.
