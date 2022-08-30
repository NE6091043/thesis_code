
# Protocol Selection
A DRL Based Network Protocol Selector in OM2M Environment
Ubuntu 18.04

## Requirement
```
Openfire==4.0.1
Californium==1.0.4
Tyrus==1.13.1
Moquette==0.7
python==3.8
tensorflow==2.9
numpy==1.20
flask==1.1.2
matplotlib==3.3.4
```

## Git
```
sudo apt-get update
sudo  apt install git
git clone https://github.com/NE6091043/protocol_selection_OM2M.git
```


## 使用方法
1. 在論文裡在各種不同的packet loss rate、不同的data size和不同bandwidth底下測試了4種協定：MQTT、CoAP、WebSocket、XMPP和random、DQN方法
2. Packet loss rate和Bandwidth需要使用WANem來做設定，WANem的使用方式在WANem的教學文件。
3. 下載MN-CSE映像檔並安裝在
### DQN protocol selection執行順序
1. 開啟DQN agent 執行 DQN.py
2. 
