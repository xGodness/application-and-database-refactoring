import "./TradeOffers.css";
import { useEffect, useState } from "react";
import axios from "axios";
import InventoryCard from "../components/InventoryCard/InventoryCard";
import Input from "../components/Input/Input";
import Button from "../components/Button/Button";
import { useNavigate } from "react-router-dom";

const TradeOffers = () => {
    
    const navigate = useNavigate();
    
    const [inventory, setInventory] = useState([]);
    const [allItems, setAllItems] = useState([]);
    
    const [input, setInput] = useState("");
    
    const [itemsOffered, setItemsOffered] = useState([]);
    const [itemsWanted, setItemsWanted] = useState([]);
    
    const [offers, setOffers] = useState([]);
    

    useEffect(() => {
        
        axios({
            method: "post",
            url: "http://localhost:8080/api/user/login",
            headers: {},
            data: {
                username: localStorage.getItem("username"),
                password: localStorage.getItem("password"),
            },
        })
      .then((r) => {
         
      })
      .catch((err) => navigate('/login'));
        
       axios({
      method: "post",
      url: "http://localhost:8080/api/item/inventory",
      headers: {},
      data: {
        username: localStorage.getItem("username"),
        password: localStorage.getItem("password"),
      },
    })
      .then((r) => setInventory(r.data.resultList))
      .catch((err) => console.log(err));

    axios({
      method: "post",
      url: "http://localhost:8080/api/item/list",
      headers: {},
      data: {},
    })
      .then((r) => setAllItems(r.data.resultList))
      .catch((err) => console.log(err));
    
    axios({
        method: "post",
        url: "http://localhost:8080/api/offer/list",
        headers: {},
        data: {},
    })
      .then((r) => setOffers(r.data.resultList))
      .catch((err) => console.log(err));

    console.log("REFRESHED");
    }, []);
    
    
    
    const handleChange = (val) => {
        setInput(val.target.value);
    };
    
    const addItemsWanted = (id) => {
 
        const item = allItems.filter((el) => el.id === id)[0];
        

        let newItemsWanted = [...itemsWanted];
        
        
        const hasItem = newItemsWanted.filter(el => el.item.id === id).length > 0;
        
        if (hasItem) {
            newItemsWanted = newItemsWanted.map(el => {
                if (el.item.id != id) return el;
                
                let newEl = {...el}
                newEl.count = newEl.count + 1;
                    
                return newEl;
            })
        }
        else {
            let newEl = {
                item: {...item},
                count: 1,
            }
            newItemsWanted.push(newEl);
        }

        setItemsWanted(newItemsWanted);
        
    };
    
    const onRemoveWanted = (id) => {
        let newItemsWanted = [...itemsWanted];
        
        let item = newItemsWanted.filter((el) => el.item.id === id)[0];
        
        if (item.count > 1) {
            let newItem = {...item}
            newItem.count -=1;
            newItemsWanted = newItemsWanted.map(el => {
                if (el.item.id === id) return newItem;
                else return el;
            })
        }
        else {
            newItemsWanted = newItemsWanted.filter((el) => el.item.id !== id);
        }
        setItemsWanted(newItemsWanted);
    };
    
    const Wanted = () => (
        <div>
            <h2>Wanted</h2>
            <p>Click on an item from items to remove it from the wanted.</p>

            <div id="wanted">
                {itemsWanted.map((el) => {
                    const item = el;
                    return (
                        <InventoryCard
                            id={item.item.id}
                            name={item.item.name}
                            displayName={item.item.displayName}
                            selectCard={onRemoveWanted}
                            count={item.count}
                        />
                        );
                })}
            </div>
        </div>
        );
    
    const Offered = () => (
        <div>
            <h2>Offered</h2>
            <p>Click on an item from items to remove it from the Offered.</p>

            <div id="offered">
                {itemsOffered.map((el) => {
                    const item = el;

                    return (
                        <InventoryCard
                            id={item.item.id}
                            name={item.item.name}
                            displayName={item.item.displayName}
                            selectCard={onRemoveOffered}
                            // selected={item.id === selectedItem}
                            count={item.count}
                        />
                        );
                })}
            </div>
        </div>
        );
    
    const Items = () => (
        <div className="itemsWrapper">
            <h2>All items</h2>
            <p>Click on an item from items to add it to the wanted.</p>
            <Input value={input} onChange={handleChange} />
            <div id="inventory">
                {allItems
          .filter((el) => {
              return el.displayName.toLowerCase().includes(input.toLowerCase());
              // return true;
          })
          .map((el) => {
              const item = el;

              return (
                  <InventoryCard
                      id={item.id}
                      name={item.name}
                      displayName={item.displayName}
                      selectCard={addItemsWanted}
                      // selected={item.id === selectedItem}
                      // count={count}
                      key={item.id}
                  />
                  );
          })}
            </div>
        </div>
        );
    
    const onRemoveOffered = (id) => {
        let newItemsOffered = [...itemsOffered];

        let item = newItemsOffered.filter((el) => el.item.id === id)[0];

        if (item.count > 1) {
            let newItem = {...item}
            newItem.count -=1;
            newItemsOffered = newItemsOffered.map(el => {
                if (el.item.id === id) return newItem;
                else return el;
            })
        }
        else {
            newItemsOffered = newItemsOffered.filter((el) => el.item.id !== id);
        }
        setItemsOffered(newItemsOffered);
        
        // BREAK
        
        item = allItems.filter((el) => el.id === id)[0];

        let newInventory = [...inventory];
        const hasItem = newInventory.filter(el => el.item.id === id).length > 0;

        if (hasItem) {
            newInventory = newInventory.map(el => {
                if (el.item.id != id) return el;

                let newEl = {...el}
                newEl.count = newEl.count + 1;

                return newEl;
            })
        }
        else {
            let newEl = {
                item: {...item},
                count: 1,
            }
            newInventory.push(newEl);
        }

        setInventory(newInventory);
        
    }
    
    const addOffered = (id) => {
        let item = allItems.filter((el) => el.id === id)[0];


        let newItemsOffered = [...itemsOffered];
        const hasItem = newItemsOffered.filter(el => el.item.id === id).length > 0;

        if (hasItem) {
            newItemsOffered = newItemsOffered.map(el => {
                if (el.item.id != id) return el;

                let newEl = {...el}
                newEl.count = newEl.count + 1;

                return newEl;
            })
        }
        else {
            let newEl = {
                item: {...item},
                count: 1,
            }
            newItemsOffered.push(newEl);
        }

        setItemsOffered(newItemsOffered);
        
        // BREAK
        
        let newInventory = [...inventory];

        item = newInventory.filter((el) => el.item.id === id)[0];

        if (item.count > 1) {
            let newItem = {...item}
            newItem.count -=1;
            newInventory = newInventory.map(el => {
                if (el.item.id === id) return newItem;
                else return el;
            })
        }
        else {
            newInventory = newInventory.filter((el) => el.item.id !== id);
        }
        setInventory(newInventory);
    }
    
    const Inventory = () => (
        <div>
            <h2>Inventory</h2>
            <p>
                Click on an ingredient from your inventory to add it to Offered.
            </p>
            <div id="inventory">
                {inventory.map((el) => {
                    const { item, count } = el;

                    return (
                        <InventoryCard
                            id={item.id}
                            name={item.name}
                            displayName={item.displayName}
                            selectCard={addOffered}
                            count={count}
                            key={item.id}
                        />
                        );
                })}
            </div>
        </div>
        );
    
    const makeOffer = () => {
        
        if (itemsWanted.length > 0 || itemsOffered.length > 0) axios({
            method: "post",
            url: "http://localhost:8080/api/offer/create",
            headers: {},
            data: {
                user: {
                    username: localStorage.getItem("username"),
                    password: localStorage.getItem("password"),
                },
                itemsWanted: itemsWanted,
                itemsOffered: itemsOffered
            },
        })
      .then((r) => {
          setItemsOffered([]);
          setItemsWanted([]);
      })
      .catch((err) => console.log(err));
    }
    
    
//    {
//        "id": 1,
//            "user": {
//            "id": 1,
//                "username": "superuser",
//                "password": null
//    },
//        "itemsWanted": [],
//            "itemsOffered": []
//    }
    const Offers = () => {
        return <div className={"offers"}>
            {offers.map(el => {
                const {id, itemsWanted, itemsOffered} = el;
         
                const acceptOffer = (el) => {
                    
                    axios({
                        method: "post",
                        url: "http://localhost:8080/api/offer/accept",
                        headers: {},
                        data: {
                            user:{
                                username: localStorage.getItem("username"),
                                password: localStorage.getItem("password"),
                            },
                            offerId: el.target.id,
                        },
                    })
                      .then((r) => console.log(r.data.resultList))
                      .catch((err) => console.log(err));
                }
                
                return <div className={"minioffer"}>
                    <h3>Wanted</h3>
                    {itemsWanted.map(el => {
                        const item = el;
                        return (
                            <InventoryCard
                                id={item.item.id}
                                name={item.item.name}
                                displayName={item.item.displayName}
                                selectCard={() => {}}
                                count={item.count}
                            />);
                    })}
                    {/*<div className="arrow">&#10132;</div>*/}
                    <h3>Offered</h3>
                    {itemsOffered.map(el => {
                        const item = el;
                        return (
                            <InventoryCard
                                id={item.item.id}
                                name={item.item.name}
                                displayName={item.item.displayName}
                                selectCard={() => {}}
                                count={item.count}
                            />);
                    })}
                    
                    <Button onClick={acceptOffer} id={id}>Accept Offer</Button>
                </div>

            })}
        </div>
    }
    
    
    return <div>
        <h1>Trade Offers</h1>
        <br/>
        <Button onClick={makeOffer}> Make Offer </Button>
        <br/>
            <div className={"sides"} >
                <div className={"offered"}>
                    <Offered />
                    <Inventory />
                </div>
                <div className={"wanted"}>
                    <Wanted />
                    <Items />
                </div>
            </div>
        <Offers />
    </div>
}

export default TradeOffers