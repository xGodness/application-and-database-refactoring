import "./InventoryCard.css";
import {useState} from "react";
const InventoryCard = (props) => {
  const { id, name, displayName, count, selected, selectCard, isAir, htmlId, ...rest } =
    props;
  
//  console.log(props);
  
  const classname =
    "gridCell " + (!isAir && selected ? `gridCellSelected` : "gridCellMain");

  const [showHint, setShowHint] = useState(false);

  const handleMouseEnter = () => {
    setShowHint(true);
  }
  const handleMouseLeave = () => {
    setShowHint(false);
  }
  return (
    <div
      className={classname}
      onClick={() => selectCard(id)}
      id={`inventory-${id}`}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      {!isAir && (
        <div id={htmlId}>
          <img
            src={`/items_pictures/${name}.png`}
            onError={({ currentTarget }) => {
              currentTarget.src = "/items_pictures/barrier.png";
//              console.log(name);
              currentTarget.onerror = null;
            }}
            alt={displayName}
          ></img>
          {count && <span className={"count"}>{count}</span>}
        </div>
      )}
      {showHint && !isAir && (
          <div
              style={{
                // position: "absolute",
                // top: "110%",
                // left: "50%",
                transform: "translateX(-10%)",
                backgroundColor: "#333",
                color: "#fff",
                width: "fit-content",
                padding: "8px 12px",
                borderRadius: "4px",
                boxShadow: "0px 4px 6px rgba(0, 0, 0, 0.1)",
                fontSize: "14px",
                whiteSpace: "nowrap",
              }}
          >
            {displayName}
          </div>
      )}
    </div>
  );
};
export default InventoryCard;
