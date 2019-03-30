#!/usr/bin/env bash
javac *.java

java HQ -ORBInitialPort 1075 -LocalServers lserver001,lserver002,lserver003 &

sleep 1

java LServer -ORBInitialPort 1075 -Name lserver003 -EntryGates entry001,entry002 -PayStations pay001,pay002,pay003 -ExitGates exit001,exit002,exit003 &

sleep 1

java LServer -ORBInitialPort 1075 -Name lserver001 -EntryGates entry003 -PayStations pay004 -ExitGates exit004 &

sleep 1

java EntryClient -ORBInitialPort 1075 -Name entry002 &

sleep 1

java EntryClient -ORBInitialPort 1075 -Name entry003 &

sleep 1

java PayStationClient -ORBInitialPort 1075 -Name pay003 &

sleep 1

java ExitClient -ORBInitialPort 1075 -Name exit001 &


sleep 1

java ExitClient -ORBInitialPort 1075 -Name exit002 &

sleep 1

java ExitClient -ORBInitialPort 1075 -Name exit004
