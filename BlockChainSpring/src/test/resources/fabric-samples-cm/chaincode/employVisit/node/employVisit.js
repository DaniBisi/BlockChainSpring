/*
# Copyright IBM Corp. All Rights Reserved.
#
# SPDX-License-Identifier: Apache-2.0
*/

'use strict';
const shim = require('fabric-shim');
const util = require('util');

let Chaincode = class {

  // The Init method is called when the Smart Contract 'fabcar' is instantiated by the blockchain network
  // Best practice is to have any Ledger initialization in separate function -- see initLedger()
  async Init(stub) {
    console.info('=========== Instantiated fabcar chaincode ===========');
    return shim.success();
  }

  // The Invoke method is called as a result of an application request to run the Smart Contract
  // 'fabcar'. The calling application program has also specified the particular smart contract
  // function to be called, with arguments
  async Invoke(stub) {
    let ret = stub.getFunctionAndParameters();
    console.info(ret);

    let method = this[ret.fcn];
    if (!method) {
      console.error('no function of name:' + ret.fcn + ' found');
      throw new Error('Received unknown function ' + ret.fcn + ' invocation');
    }
    try {
      let payload = await method(stub, ret.params);
      return shim.success(payload);
    } catch (err) {
      console.log(err);
      return shim.error(err);
    }
  }

  async queryCar(stub, args) {
    if (args.length != 1) {
      throw new Error('Incorrect number of arguments. Expecting CarNumber ex: CAR01');
    }
    let carNumber = args[0];

    let carAsBytes = await stub.getState(carNumber); //get the car from chaincode state
    if (!carAsBytes || carAsBytes.toString().length <= 0) {
      throw new Error(carNumber + ' does not exist: ');
    }
    console.log(carAsBytes.toString());
    return carAsBytes;
  }
 
  async initLedger(stub, args) {
    console.info('============= START : Initialize Ledger ===========');
    let visits = [];
    visits.push({
      IDhash: 'ec363bd2b1c7715e48ace871bb7fe64820ace657',
      name: 'Admin',
      agency: 'Magenta',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: '158ec78ab7b9190385379230587a7ca8e0d33383',
      name: 'User2',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: '1b740424702841d0529c9af5c8478d25ead55379',
      name: 'User1',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: 'f480397b54c1b0f9bc403985eb4700ae1b1c672f',
      name: 'User2',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: '18281e6f75fe1412d0f2afa2894050677e4c456a',
      name: 'Admin',
      agency: 'Magenta',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: 'ee24822909bb95354f3f43c964a4b988c7de66a6',
      name: 'User1',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: '343af2b08f306b8b407dd6d57d7a95b699479d1e',
      name: 'User1',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: 'e13b501f310f19651757d46555223c82f6fdc4c1',
      name: 'User2',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: 'a8ed3cac9e9280b664e409b6c5e16ef969ad7f5a',
      name: 'Admin',
      agency: 'Magenta',
      date: '10-04-18:9.47'
    });
    visits.push({
      IDhash: '8c88abef1efda183eb66475f001ff4ea200266f9',
      name: 'User2',
      agency: 'Unifi',
      date: '10-04-18:9.47'
    });

    for (let i = 0; i < visits.length; i++) {
      visits[i].docType = 'visit';
      await stub.putState(i, Buffer.from(JSON.stringify(visits[i])));
      console.info('Added <--> ', visits[i]);
    }
    console.info('============= END : Initialize Ledger ===========');
  }

  async createCar(stub, args) {
    console.info('============= START : Create Car ===========');
    if (args.length != 5) {
      throw new Error('Incorrect number of arguments. Expecting 5');
    }



    var visit = {
      docType: 'visit',
      IDhash: args[1],
      name: args[2],
      agency: args[3],
      date: args[4]
    };
    var lastInsertedId = stub.getState(lastInsertedId);
    var lastIdInt = parseInt(lastInsertedId);
    var lastInsertedId = (lastIdInt +1).toString();
    await stub.putState(lastInsertedId, Buffer.from(JSON.stringify(car)));
    console.info('============= END : Create Car ===========');
  }

  async queryAllCars(stub, args) {

    let startKey = '0';
    let endKey = '999';

    let iterator = await stub.getStateByRange(startKey, endKey);

    let allResults = [];
    while (true) {
      let res = await iterator.next();

      if (res.value && res.value.value.toString()) {
        let jsonRes = {};
        console.log(res.value.value.toString('utf8'));

        jsonRes.Key = res.value.key;
        try {
          jsonRes.Record = JSON.parse(res.value.value.toString('utf8'));
        } catch (err) {
          console.log(err);
          jsonRes.Record = res.value.value.toString('utf8');
        }
        allResults.push(jsonRes);
      }
      if (res.done) {
        console.log('end of data');
        await iterator.close();
        console.info(allResults);
        return Buffer.from(JSON.stringify(allResults));
      }
    }
  }

  async changeCarOwner(stub, args) {
    console.info('============= START : changeCarOwner ===========');
    if (args.length != 2) {
      throw new Error('Incorrect number of arguments. Expecting 2');
    }

    let visitAsBytes = await stub.getState(args[0]);
    let visit = JSON.parse(vistAsBytes);
    visit.date = args[1];

    await stub.putState(args[0], Buffer.from(JSON.stringify(visits)));
    console.info('============= END : changeCarOwner ===========');
  }
};

shim.start(new Chaincode());
