import "./AdminPage.css";
import { useState, useEffect } from "react";
import InventoryCard from "../components/InventoryCard/InventoryCard";
import axios from "axios";

import { useNavigate } from "react-router-dom";
import Input from "../components/Input/Input";
import Button from "../components/Button/Button";

const AdminPage = () => {
    const navigate = useNavigate();

    const [craftingTableArray, setCraftingTableArray] = useState([
        0, 0, 0, 0, 0, 0, 0, 0, 0,
    ]);
    const [resultItem, setResultItem] = useState(null);

    const [selectedItem, setSelectedItem] = useState(null);

    const [allItems, setAllItems] = useState([]);

    const [recipeError, setRecipeError] = useState(null);

    useEffect(() => {

        axios({
            method: "post",
            url: "http://localhost:8080/api/user/amiadmin",
            headers: {},
            data: {
                username: localStorage.getItem("username"),
                password: localStorage.getItem("password"),
            },
        })
            .then(() => {

            })
            .catch(() => navigate('/login'));

        setCraftingTableArray([0, 0, 0, 0, 0, 0, 0, 0, 0]);
        setResultItem(null);

        axios({
            method: "post",
            url: "http://localhost:8080/api/item/list",
            headers: {},
            data: {},
        })
            .then((r) => setAllItems(r.data.resultList))
            .catch((err) => console.log(err));

        console.log("REFRESHED");
    }, []);

    const selectItem = (item) => {
        if(item === selectedItem) setSelectedItem(null);
        else setSelectedItem(item);
    }

    const selectCell = (num)=>{

        let tempCraftingArray = [...craftingTableArray];
        tempCraftingArray[num] = selectedItem && selectedItem !== craftingTableArray[num]  ? selectedItem : 0;
        setCraftingTableArray(tempCraftingArray);
    }

    const selectResult = () => {
        setResultItem(selectedItem && selectedItem !== resultItem ? selectedItem : null)
    }

    const createRecipe = () => {
        axios({
            method: "post",
            url: "http://localhost:8080/api/recipe/save",
            headers: {},
            data: {
                user: {
                    username: localStorage.getItem("username"),
                    password: localStorage.getItem("password"),
                },
                recipe:{
                    craftMatrix: [
                        [craftingTableArray[0], craftingTableArray[1], craftingTableArray[2]],
                        [craftingTableArray[3], craftingTableArray[4], craftingTableArray[5]],
                        [craftingTableArray[6], craftingTableArray[7], craftingTableArray[8]],
                    ],
                    resultItemId: resultItem,
                    resultItemCnt:1
                }

            },
        })
            .then(() => navigate(0))
            .catch((err) => {
                setRecipeError(err.response.data.errorList[0])
            });
    }


    const Result = () => {
        if (resultItem === null) {
            return (
                <div className={"Result"}>
                    <InventoryCard
                        id={0}
                        selectCard={selectResult}
                        key={"craft-res-" + 0}
                        isAir={true}
                    />
                </div>
            );
        }

        const item = allItems.filter(el => el.id === resultItem)[0]

        return (
            <div className="Result">
                <InventoryCard
                    id={item.id}
                    name={item.name}
                    displayName={item.displayName}
                    selectCard={selectResult}
                    // count={resultItem.count}
                    htmlId={"result"}
                />
            </div>
        );
    };

    const CraftingTable = () => (
        <div>
            <h1>New Recipe</h1>
            <div className={"recipe"}>
                <div id="grid">
                    {craftingTableArray.map((el, i) => {
                        if (el === 0 || allItems === []) {
                            return (
                                <InventoryCard
                                    id={i}
                                    selectCard={selectCell}
                                    key={"craft-" + i}
                                    isAir={true}
                                />
                            );
                        }

                        const item = allItems.filter((item) => item.id === el)[0];

                        return (
                            <InventoryCard
                                id={i}
                                name={item.name}
                                displayName={item.displayName}
                                selectCard={selectCell}
                                key={"craftTable-" + i}
                                htmlId={"craftTable-" + i}
                            />
                        );
                    })}
                </div>

                <div className="arrow">&#10132;</div>
                <Result />
            </div>
        </div>
    );

    return (
        <div >
            <div id="screen" style={{overflowX: "hidden", overflowY: "hidden", paddingBottom: "20px"}}>
                <OpUser />
                <CraftingTable />
                <Button onClick={createRecipe}>Create Recipe</Button>
                {recipeError && (<div style={{marginTop:"10px"}}>{recipeError}</div>)}
                <Items allItems={allItems} selectItem={selectItem} selectedItem={selectedItem}/>
            </div>
        </div>
    );
};

const OpUser = () => {

    const [input, setInput] = useState("");
    const [error, setError] = useState(null);

    const handleChange = (val) => {
        setInput(val.target.value);
    };

    const opRequest = () => {
        axios({
            method: "post",
            url: `http://localhost:8080/api/user/op/${input}`,
            headers: {},
            data: {
                    username: localStorage.getItem("username"),
                    password: localStorage.getItem("password")
            },
        })
            .then(() => {})
            .catch((err) => {
                setError(err.response.data.errorList[0])
            });
    }

    return (
        <div>
            <h1>Op User</h1>
            <Input value={input} onChange={handleChange} style={{marginBottom: "10px"}} placeholder={"username"} />
            {error && (<div style={{paddingBottom:"10px"}}>{error}</div>)}
            <div><Button onClick={opRequest}>Make Admin</Button></div>
        </div>
    )
}

const Items = ({allItems, selectItem, selectedItem}) => {

    const [input, setInput] = useState("");
    const handleChange = (val) => {
        setInput(val.target.value);
    };

    return (
        <div className="itemsWrapper">
            <h2>All items</h2>
            <Input value={input} onChange={handleChange} style={{marginBottom: "10px"}}/>
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
                                selectCard={selectItem}
                                selected={item.id === selectedItem}
                                // count={count}
                                key={item.id}
                            />
                        );
                    })}
            </div>
        </div>
    )
};

export default AdminPage;
