TTVP 3+4 (Sea Battle with CHORD)
===================
**Technik & Technologie vernetzter Systeme**

                          __    __    __
						 |==|  |==|  |==|
                       __|__|__|__|__|__|_
					__|___________________|___
                 __|__[]__[]__[]__[]__[]__[]__|___
				|............................o.../
                \.............................../
			~')_,~')_,~')_,~')_,~')_,~')_,~')_,~')/,~')_
                                Sea Battle
							 
Distributed Sea Battle game on the Peer-to-Peer Chord algorithm

> **Note:** 
> - The specification can be found in the official lab specifications!<sup>[1](#myfootnote1)</sup>

## <i class="icon-desktop"></i> Start bootstrap node (server):

```
> java -jar .\starter.jar create BootstrapIPAddress:BootstrapPort
```

## <i class="icon-desktop"></i> Start peer node (client):

```
> java -jar .\starter.jar join BootstrapIPAddress:BootstrapPort PeerIPAddress:PeerPort
```



<a name="myfootnote1">1</a>: http://inet.cpt.haw-hamburg.de/teaching/ws-2015-16/technik-und-technologie-vernetzter-systeme/lab2.