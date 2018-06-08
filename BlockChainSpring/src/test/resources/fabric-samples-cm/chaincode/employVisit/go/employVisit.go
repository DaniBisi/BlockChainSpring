
package main

/* Imports
 * 5 utility libraries for formatting, handling bytes, reading and writing JSON, and string manipulation
 * 2 specific Hyperledger Fabric specific libraries for Smart Contracts
 */
import (
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	sc "github.com/hyperledger/fabric/protos/peer"
)

// Define the Smart Contract structure
type SmartContract struct {
}

// Define the visit structure, with 4 properties.  Structure tags are used by encoding/json library
type Visit struct {
	IDHash string `json:"IDhash"`
	Name   string `json:"name"`
	Agency string `json:"agency"`
	Date   string `json:"date"`
	Time   string `json:"time"`
}

/*
 * The Init method is called when the Smart Contract "fabVisit" is instantiated by the blockchain network
 * Best practice is to have any Ledger initialization in separate function -- see initLedger()
 */
func (s *SmartContract) Init(APIstub shim.ChaincodeStubInterface) sc.Response {
	return shim.Success(nil)
}

/*
 * The Invoke method is called as a result of an application request to run the Smart Contract "fabVisit"
 * The calling application program has also specified the particular smart contract function to be called, with arguments
 */
func (s *SmartContract) Invoke(APIstub shim.ChaincodeStubInterface) sc.Response {

	function, args := APIstub.GetFunctionAndParameters()
	if function == "queryVisit" {
		return s.queryVisit(APIstub, args)
	} else if function == "initLedger" {
		return s.initLedger(APIstub)
	} else if function == "lastInsertedId" {
		return s.querylastInsertedId(APIstub)
	} else if function == "createVisit" {
		return s.createVisit(APIstub, args)
	}  else if function == "updateVisit" {
		return s.updateVisit(APIstub, args)
	} else if function == "queryAllVisits" {
		return s.queryAllVisits(APIstub,args)
	} else if function == "changeVisitOwner" {
		return s.changeVisitOwner(APIstub, args)
	}

	return s.queryAllVisits(APIstub,args)
}

func (s *SmartContract) queryVisit(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments. Expecting 1")
	}

	VisitAsBytes, _ := APIstub.GetState(args[0])
	return shim.Success(VisitAsBytes)
}

func (s *SmartContract) querylastInsertedId(APIstub shim.ChaincodeStubInterface) sc.Response {
	lastInsertedId, _ := APIstub.GetState("lastInsertedId")
	
	lastIdAsString := string(lastInsertedId[:])
	var args1 = []string {lastIdAsString}
	return s.queryAllVisits(APIstub,args1)
	
}

func (s *SmartContract) initLedger(APIstub shim.ChaincodeStubInterface) sc.Response {
	Visits := []Visit{
		Visit{IDHash: "6f83a90e58a43520a3f7214328091be808ab9177a4dfad9dcc3107ea81db607d", Name: "Admin", Agency: "Magenta3.76", Date: "2018-04-10", Time: "09:49"},
		Visit{IDHash: "cc3b2febe0c83c141c5405daece2f3db67decf4c66c43c9669913996c3c32743", Name: "User2", Agency: "Unifi", Date: "2018-04-10", Time: "09:48"},
		Visit{IDHash: "9814c045246960a84f1abdf51b598a09222a2a1f34d8ad911206b8c40bea97d8", Name: "User1", Agency: "Unifi", Date: "2018-04-10", Time: "09:47"},
		Visit{IDHash: "d49bd7a0d468b41f83df7340c1ae39a9f6cd2f667a890f39d4ce22ad6da0d3fb", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:46"},
		Visit{IDHash: "68581372af425b3243bfde322cff5bb81c2c1d873449f77e9cdeea8c5be113b4", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:45"},
		Visit{IDHash: "9fd2ed5905373e8d6d36c1da69fba36aca1939dfe7aa90efb5cba4c8f54f2b5a", Name: "User2", Agency: "Unifi", Date: "2018-04-10", Time: "09:44"},
		Visit{IDHash: "cf3409808cba562eecaf899db498e35ac374ffd5c6e37483facbe49248d750e4", Name: "User1", Agency: "Unifi", Date: "2018-04-10", Time: "09:43"},
		Visit{IDHash: "3d3f05ece8f1f5378bc7d9bf0220c26f7974f3ca7fb180eae8c0f17497ecf04f", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:42"},
		Visit{IDHash: "ff323d55115a0eabee90e28c6c918316459f724123be089a70f969ce6404cf2a", Name: "User2", Agency: "Unifi", Date: "2018-04-10", Time: "09:41"},
		Visit{IDHash: "80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:40"},
		Visit{IDHash: "524d9a207e81aff81d050e7ec134642a1ff13f39635542148729dddbc79cd68e", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:39"},
		Visit{IDHash: "95763f91f5fc8e956f7565476bb6b77af6bfb870288901f3c18e4d7ad08d606f", Name: "Admin", Agency: "Magenta", Date: "2018-04-10", Time: "09:38"},
	}
	i := 0
	for i < len(Visits) {
		fmt.Println("i is ", i)
		VisitAsBytes, _ := json.Marshal(Visits[i])
		APIstub.PutState(strconv.Itoa(i), VisitAsBytes)
		fmt.Println("Added", Visits[i])
		i = i + 1
	}
	var iasString = strconv.Itoa(i-1)
	APIstub.PutState("lastInsertedId", []byte(iasString))
	var buffer bytes.Buffer
	buffer.WriteString("[Vittoria" + iasString+"]")
	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) createVisit(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) < 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var Visit = Visit{IDHash: args[0], Name: args[1], Agency: args[2], Date: args[3],Time: args[4]}

	VisitAsBytes, _ := json.Marshal(Visit)
	var liIDS = ""
	if(len(args)==5 || args[5]==""){
	var liIDAsByte, _ = APIstub.GetState("lastInsertedId")
	liIDS = string(liIDAsByte[:])
	var liID, _ = strconv.Atoi(liIDS)
	liID = liID + 1
	liIDS = strconv.Itoa(liID)
	APIstub.PutState("lastInsertedId", []byte(liIDS)) //salvo l'id dell ultima visita effettuata
	}else {
		liIDS = args[5]
	}
	fmt.Printf("new visit created %s", liIDS)
	APIstub.PutState(liIDS, VisitAsBytes)             // memorizzo la visita in una chiave del tipo "2" -> visit
	
	return shim.Success([]byte(liIDS))
}


func (s *SmartContract) updateVisit(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 5 {
		return shim.Error("Incorrect number of arguments. Expecting 5")
	}

	var Visit = Visit{IDHash: args[0], Name: args[1], Agency: args[2], Date: args[3],Time: args[4]}

	VisitAsBytes, _ := json.Marshal(Visit)
	var liIDS = args[5]
	fmt.Printf("new visit created %s", liIDS)
	APIstub.PutState(liIDS, VisitAsBytes)
	
	return shim.Success([]byte(liIDS))
}

func (s *SmartContract) queryAllVisits(APIstub shim.ChaincodeStubInterface , args []string) sc.Response {
	
	startKey := "0"
	var endKeyS = "999" //string(endKey[:])
	var endKey = 0
	if len(args) != 0 {
		startKey = args[0]
		// endKeyS = args[0]
		endKey, _  = strconv.Atoi(startKey)
		endKey = endKey + 1
		endKeyS = strconv.Itoa(endKey)
	}
	resultsIterator, err := APIstub.GetStateByRange(startKey, endKeyS)
	if err != nil {
		return shim.Error(err.Error())
	}
	defer resultsIterator.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")
	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return shim.Error(err.Error())
		}
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
	}
	buffer.WriteString("]")

	fmt.Printf("- queryAllVisits:\n%s\n", buffer.String())

	return shim.Success(buffer.Bytes())
}

func (s *SmartContract) changeVisitOwner(APIstub shim.ChaincodeStubInterface, args []string) sc.Response {

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments. Expecting 2")
	}

	VisitAsBytes, _ := APIstub.GetState(args[0])
	Visit := Visit{}

	json.Unmarshal(VisitAsBytes, &Visit)
	Visit.Name = args[1]

	VisitAsBytes, _ = json.Marshal(Visit)
	APIstub.PutState(args[0], VisitAsBytes)

	return shim.Success(nil)
}

// The main function is only relevant in unit test mode. Only included here for completeness.
func main() {
	err := shim.Start(new(SmartContract))
	if err != nil {
		fmt.Printf("Error creating new Smart Contract: %s", err)
	}

}
