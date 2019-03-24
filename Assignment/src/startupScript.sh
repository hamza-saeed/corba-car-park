javac *.java

java HQ -ORBInitialPort 1075 -LocalServers lserver001,lserver002,lserver003 &

sleep .5

java LServer -ORBInitialPort 1075 -Name lserver003 -EntryGates entry001,entry002-PayStations pay001,pay002,pay003 &

sleep .5

java LServer -ORBInitialPort 1075 -Name lserver001 -EntryGates entry003 -PayStations pay001 &


sleep .5

java EntryClient -ORBInitialPort 1075 -Name entry002 &

sleep .5

java EntryClient -ORBInitialPort 1075 -Name entry003 &

sleep .5

java PayStationClient -ORBInitialPort 1075 -Name pay003



