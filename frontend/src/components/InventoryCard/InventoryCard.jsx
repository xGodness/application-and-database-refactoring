import "./InventoryCard.css";
const InventoryCard = (props) => {
  const { id, name, displayName, count, selected, selectCard, isAir, htmlId } =
    props;
  
//  console.log(props);
  
  const classname =
    "gridCell " + (!isAir && selected ? `gridCellSelected` : "gridCellMain");

  return (
    <div
      className={classname}
      onClick={() => selectCard(id)}
      id={`inventory-${id}`}
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
    </div>
  );
};
export default InventoryCard;
