#!/bin/bash

# === Environment Setup ===
export PATH=${PWD}/../bin:${PWD}:$PATH
export FABRIC_CFG_PATH=${PWD}/../config/
export CORE_PEER_TLS_ENABLED=true

export ORDERER_CA=${PWD}/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com>
export ORDERER_ADDRESS=localhost:7050

export CORE_PEER_TLS_ROOTCERT_FILE_ORG1=${PWD}/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.c>
export CORE_PEER_TLS_ROOTCERT_FILE_ORG2=${PWD}/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.c>

CHANNEL_NAME="mychannel"
CHAINCODE_NAME="CourierSystem"
CHAINCODE_VERSION="1"
CHAINCODE_PATH="../../eclipse-workspace/CourierSystem/lib/build/install/lib"
CHAINCODE_LANG="java"
CHAINCODE_LABEL="CourierSystem_1"

# === Peer Environments ===
setEnvVarsForPeer0Org1() {
    export CORE_PEER_LOCALMSPID="Org1MSP"
    export CORE_PEER_TLS_ROOTCERT_FILE=$CORE_PEER_TLS_ROOTCERT_FILE_ORG1
    export CORE_PEER_MSPCONFIGPATH=${PWD}/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
    export CORE_PEER_ADDRESS=localhost:9051
}

# === Chaincode Lifecycle ===
packageChaincode() {
    echo "===== Packaging chaincode ====="
    rm -rf ${CHAINCODE_NAME}.tar.gz
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode package ${CHAINCODE_NAME}.tar.gz \
        --path ${CHAINCODE_PATH} --lang ${CHAINCODE_LANG} --label ${CHAINCODE_LABEL}
    echo "===== Chaincode packaged ====="
}

installChaincode() {
    echo "===== Installing on peer0.org1 ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode install ${CHAINCODE_NAME}.tar.gz

    echo "===== Installing on peer0.org2 ====="
    setEnvVarsForPeer0Org2
    peer lifecycle chaincode install ${CHAINCODE_NAME}.tar.gz
}

queryInstalled() {
    echo "===== Query installed chaincode ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode queryinstalled > log.txt
    cat log.txt
    PACKAGE_ID=$(sed -n "/${CHAINCODE_LABEL}/{s/^Package ID: //; s/, Label:.*$//; p;}" log.txt)
    echo "Package ID: ${PACKAGE_ID}"
}

approveForMyOrg1() {
    echo "===== Approving for Org1 ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode approveformyorg -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA \
        --channelID $CHANNEL_NAME --name $CHAINCODE_NAME --version $CHAINCODE_VERSION \
        --init-required --package-id $PACKAGE_ID --sequence 1
}

approveForMyOrg2() {
    echo "===== Approving for Org2 ====="
    setEnvVarsForPeer0Org2
    peer lifecycle chaincode approveformyorg -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA \
        --channelID $CHANNEL_NAME --name $CHAINCODE_NAME --version $CHAINCODE_VERSION \
        --init-required --package-id $PACKAGE_ID --sequence 1
}

checkCommitReadiness() {
    echo "===== Checking commit readiness ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode checkcommitreadiness \
        --channelID $CHANNEL_NAME --name $CHAINCODE_NAME --version $CHAINCODE_VERSION \
        --sequence 1 --output json --init-required
}

commitChaincodeDefinition() {
    echo "===== Committing chaincode definition ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode commit -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA \
        --channelID $CHANNEL_NAME --name $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        --version $CHAINCODE_VERSION --sequence 1 --init-required
}

queryCommitted() {
    echo "===== Querying committed chaincode ====="
    setEnvVarsForPeer0Org1
    peer lifecycle chaincode querycommitted --channelID $CHANNEL_NAME --name $CHAINCODE_NAME
}

chaincodeInvokeInit() {
    echo "===== Invoking chaincode init ====="
    setEnvVarsForPeer0Org1
    peer chaincode invoke -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com --tls --cafile $ORDERER_CA \
        -C $CHANNEL_NAME --name $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        --isInit -c '{"Args":[]}'
}

chaincodeInvokeInitLedger() {
    echo "===== Invoking initLedger() function ====="
    setEnvVarsForPeer0Org1
    peer chaincode invoke -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com \
        --tls --cafile $ORDERER_CA \
        -C $CHANNEL_NAME -n $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        -c '{"Args":["initLedger"]}'
}

chaincodeQueryParcel() {
    echo "===== Querying Parcel ID P001 ====="
    setEnvVarsForPeer0Org1
    peer chaincode query -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":["queryParcelById", "P001"]}'

    echo "===== Querying Parcel ID P002 ====="
    peer chaincode query -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":["queryParcelById", "P002"]}'

    echo "===== Querying Parcel ID P003 ====="
    peer chaincode query -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":["queryParcelById", "P003"]}'

    echo "===== Querying Parcel ID P004 ====="
    peer chaincode query -C $CHANNEL_NAME -n $CHAINCODE_NAME -c '{"Args":["queryParcelById", "P004"]}'
}

chaincodeInvokeAddParcel() {
    echo "===== Invoking addParcel() function ====="
    setEnvVarsForPeer0Org1
    peer chaincode invoke -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com \
        --tls --cafile $ORDERER_CA \
        -C $CHANNEL_NAME -n $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        -c '{"Args":["addParcel", "P005", "John", "Emily", "Pending", "express", "1 day", "true", "12.5", "", "", "", "", ""]}'
}

chaincodeInvokeUpdateParcelStatus() {
    echo "===== Invoking updateParcelStatus() function ====="
    setEnvVarsForPeer0Org1
    peer chaincode invoke -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com \
        --tls --cafile $ORDERER_CA \
        -C $CHANNEL_NAME -n $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        -c '{"Args":["updateParcelStatus", "P005", "Delivered"]}'
}

chaincodeInvokeTransferParcelOwnership() {
    echo "===== Invoking transferParcelOwnership() function ====="
    setEnvVarsForPeer0Org1
    peer chaincode invoke -o $ORDERER_ADDRESS \
        --ordererTLSHostnameOverride orderer.example.com \
        --tls --cafile $ORDERER_CA \
        -C $CHANNEL_NAME -n $CHAINCODE_NAME \
        --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG1 \
        --peerAddresses localhost:9051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE_ORG2 \
        -c '{"Args":["transferParcelOwnership", "P005", "Michael"]}'
}

# === Run Steps ===
packageChaincode
installChaincode
queryInstalled
approveForMyOrg1
approveForMyOrg2
checkCommitReadiness
commitChaincodeDefinition
queryCommitted
sleep 5
chaincodeInvokeInit
sleep 5
chaincodeInvokeInitLedger
sleep 5
chaincodeQueryParcel
sleep 5
chaincodeInvokeAddParcel
sleep 5
chaincodeInvokeTransferParcelOwnership
sleep 5
chaincodeInvokeUpdateParcelStatus





