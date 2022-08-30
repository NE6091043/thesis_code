
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


## System Architecture
![experiment setup (2) (pdf io)](https://user-images.githubusercontent.com/72957935/187348926-ccd4b24d-c016-4d48-ad50-3cc7e096b2e9.png)



## 使用方法
1. 在論文裡在各種不同的packet loss rate、不同的data size和不同bandwidth底下測試了4種協定：MQTT、CoAP、WebSocket、XMPP和random、DQN方法
2. Packet loss rate和Bandwidth需要使用WANem來做設定，WANem的使用方式在WANem的教學文件。
3. 下載MN-CSE映像檔並在Desktop2使用Vmware安裝在VM1
4. 下載IN-CSE映像檔並在Desktop2使用Vmware安裝在VM2
5. 下載WANem映像檔並在Desktop2使用Vmware安裝在VM3
6. DA和NA分別為在Desktop1的兩個JAVA程式，直接執行就可
### DQN protocol selection event-driven application 執行順序
1. 開啟DQN agent 執行 DQN.py
2. 開啟IN-CSE
3. 開啟MN-CSE
4. 執行NA，可以訂閱到MN-CSE
5. 執行DA開始create contentinstance到MN-CSE，MN-CSE通知NA
6. 測試event-driven application時，DA需設定兩個參數，其中LoopCount是DA送幾次data給MN-CSE，而Datasize可根據LoopCount去做調整
![image](https://user-images.githubusercontent.com/72957935/187351551-126b8d7d-3d38-4c7a-a712-535c02382c8e.png)
7. 透過WANem 調整網路上的bandwidth和loss rate
![image](https://user-images.githubusercontent.com/72957935/187352698-03cb056e-e011-4e11-9d20-c76451c2ad9a.png)
8. OM2M將bandwidth和loss rate資訊傳送到DQN Agent並得到要執行的Action，也就是選擇的protocol
9. 最後NA收集實驗數據並匯出![image](https://user-images.githubusercontent.com/72957935/187353577-a8e34c0c-faad-4f6f-b4ab-807c18161489.png)





### DQN protocol selection query-driven application 執行順序
1. 開啟DQN agent 執行 DQN.py
2. 開啟IN-CSE
3. 開啟MN-CSE
4. 執行NA，準備送commands到DA
5. 執行DA開始接收commands
6. 測試query-driven application時，NA需設定兩個參數，其中LoopCount是NA送幾次commands給MN-CSE，而Datasize可根據LoopCount去做調整
![image](https://user-images.githubusercontent.com/72957935/187358536-71e3fe86-d637-46f6-b435-cc7b6a5a87f1.png)
7. 透過WANem 調整網路上的bandwidth和loss rate
![image](https://user-images.githubusercontent.com/72957935/187352698-03cb056e-e011-4e11-9d20-c76451c2ad9a.png)
8. OM2M將bandwidth和loss rate資訊傳送到DQN Agent並得到要執行的Action，也就是選擇的protocol
9. 最後DA收集實驗數據並匯出![image](https://user-images.githubusercontent.com/72957935/187359020-8458650b-8804-46f6-87b7-c9d2ca125951.png)


## Git
```
sudo apt-get update
sudo  apt install git
git clone https://github.com/NE6091043/protocol_selection_OM2M.git
```
