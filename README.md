# Application and database refactoring

In this repository the 'Information systems and databases' coursework project is being refactored.

## About the project

It is a Minecraft themed web application that is focused on crafting and trading.

### Main concepts

#### Users
* User can register account with username and password and log in afterwards.
* User can be an admin.

#### Admins
* Admins have extended privileges. They can:
    * make another user admin;
    * create new crafting recipe.

#### Inventory
* Each user has their own inventory.
* Users can obtain items by opening the chest (clicking button on the UI).
* Users can craft new items using items they have in their inventory.

#### Crafting
* New item can be crafted by placing items on the crafting table (3x3 grid) in a certain pattern.
* There is a number of recipes that are available by default (migrated from the Minecraft itself).
* New recipes can be added by any user with admin privileges.
* User can browse recipe catalogue and filter available recipes by specifying certain items.

#### Trading
* Each user can place a trade offer.
* Trade offer consists of items that are offered and items that are wanted.
* Once user has placed an offer, items that are being offered no longer available for them.
* User can accept other user's trade offer if they have items that are wanted by this offer.
* User can browse offer list and filter offers by specifying certain items.