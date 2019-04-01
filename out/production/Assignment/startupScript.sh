#!/usr/bin/env bash

javac *.java

java HQ -ORBInitialPort 1075 &

sleep 1

java LServer -ORBInitialPort 1075 -Name lserver001 -Spaces 150 &

sleep 1

java LServer -ORBInitialPort 1075 -Name lserver002 -Spaces 50 &

sleep 1

java EntryGateClient -ORBInitialPort 1075 -Name entry001 -LocalServer lserver001 &

sleep 1

java EntryGateClient -ORBInitialPort 1075 -Name entry002 -LocalServer lserver002 &

sleep 1

java PayStationClient -ORBInitialPort 1075 -Name pay001 -LocalServer lserver001 &

sleep 1.5

java PayStationClient -ORBInitialPort 1075 -Name pay002 -LocalServer lserver001 &

sleep 1.5

java ExitGateClient -ORBInitialPort 1075 -Name exit001 -LocalServer lserver001 &

sleep 1

java ExitGateClient -ORBInitialPort 1075 -Name exit002 -LocalServer lserver002