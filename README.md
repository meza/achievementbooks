# Achievement Books

The Achivement Books mod introduces the ability to create * multiple * achievement books for Minecraft.

It is highly configurable via JSON to have headers, free flow text and achievement checklists on multiple pages.

## Why use this mod?

- you can have multiple achievement books for different parts of your game
- you don't need to deal with difficult formatting files
- you can describe your books in an easy to read format
- you can upgrade your configs without the need to reset the book process
- you can reload the books from the config without restarting the game

## User Manual

### Installing the Mod

Currently Achievement Books only supports 1.7.10, but is under active development for all other versions too.

To install it, head over to the project's page on [CurseForge https://minecraft.curseforge.com/projects/achievement-books](https://minecraft.curseforge.com/projects/achievement-books)
 and download it into your mincraft installation's `mods` folder.

Note, that this is the same process for servers or clients.

> If your minecraft game is running, you need to restart it for the mod to be loaded into the game.

### Getting Started

As the mod starts the first time, you will have an example config created for you.

You can access the achievement book by:
```
/achievementbooks give
```

This will give you a mini achievement list on how to get to know the mod better.

This book is configured at ```config/achievementbooks/demo.json```
I recommend you go through the book in the game and do all the exercises there to fully understand what the mod is or is not.

### Commands

The mod listens to
```
/achievementbooks <command>
```
or in short:
```
/ab <command>
```

#### Give
```
/achievementbooks give
```
Gives the player _every_ book registered in the system

#### Import
```
/achievementbooks import
```
If you have the `Simple Achievements` mod installed already and would like to start using this instead, the import
command can create a json file for you.
The result is not going to be perfect due to formatting issues, but it's going to be close enough not to cause a big
pain to migrate.

#### Init
```
/achievementbooks init
```
Re-creates the demo config file

#### List
```
/achievementbooks list
```
Lists all registered books and their IDs

#### Reload
```
/achievementbooks reload
```
Reloads the configuration from file. Your current books will be updated, and new books will be added.
If you deleted books, those will currently only disappear if you restart the game. I'm working on fixing this.


### Configuration

All books are configured in an easy to read JSON file. The contents of the file represent a JSON Object, so it must start with a `{`
and must and with a `}`.

Like this:
```JSON
{}
```

The most minimal valid book is as follows:
```JSON
{
	"itemName": "book_smallest",
	"bookName": "The smallest book",
	"pages": []
}
```

#### `itemName` - required
This identifies the book in the item registry of the game. This needs to be unique for every book you create.
In the game, you'll be able to use: ```/give @p achievementbooks:book_smallest``` if you wish
Since it's used to identify books in sava data too, if you change this after you've started using the mod, you may lose
your progress.

#### `bookName` - required
A book's name is the most important feature of the book. This is how it shows up in the game, and this is how it's identified.
Unfortunately this means that the only thing that cannot be edited easily after the fact, is the book name. So choose carefully.

#### `craftingMaterial` - optional
Every book in the Achievement Books mod can be configure to be craftable or not. If you specify a crafting material in a `mod:item` format,
then the book will be craftable with that material and a book.
For example if you specify: `minecraft:cobblestone` as the crafting material, then if you put a regular book and a cobblestone in a crafting grid
next to eachother, it will create your book.

#### `color` / `colour` - optional
There are currently 16 colours you can use for your book. These are:

Colour parameter | Book icon
--------------- | ---------
aqua | ![aqua](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-aqua.png)
black | ![black](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-black.png)
blue | ![blue](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-blue.png)
brown | ![brown](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-brown.png)
deepblue | ![deepblue](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-deepblue.png)
gray | ![gray](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-gray.png)
green | ![green](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-green.png)
lime | ![lime](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-lime.png)
olive | ![olive](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-olive.png)
orange | ![orange](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-orange.png)
orangered | ![orangered](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-orangered.png)
peach | ![peach](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-peach.png)
pink | ![pink](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-pink.png)
purple | ![purple](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-purple.png)
red | ![red](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-red.png)
yellow | ![yellow](https://github.com/meza/achievementbooks/raw/colors/src/main/resources/assets/achievementbooks/textures/items/book-yellow.png)
 
```JSON
 {
 	"itemName": "coloured_book_smallest",
 	"colour": "orange",
 	"bookName": "The smallest book",
 	"pages": []
 }
 ```

>Yes, you can use both `color` and `colour`. They work the same way. 

#### `pages` - required
The second most important part of the book is the list of pages it has.
This is an array of arrays of page items, which means that the general format is:
```JSON
{
	"itemName": "book_example",
	"bookName": "Page example book",
	"pages": [
		[],
		[],
		[]
	]
}
```

This adds 3 empty pages to the book.
You can populate each page with a list of page items. Their configuration is as follows:

#### Page Items
Page items are JSON Objects again, which means they all need to be surrounded by `{` and `}`.
There are currently 3 types of elements that you can define:

##### Header
A header item that can be used to add som context for the user. Maybe a story line, or some explanation on why they should
complete the achievements.
```JSON
{
	"id": 1,
	"header": "Welcome to the book of achievements",
	"description": "This is a subsection for the header and completely optional"
}
```

###### `id` - required
The `id` is required to be a **unique number** for every page item, as this identifies them later. The order is not important,
they just have to be unique.

###### `header` - required
The text that you would like to see as bold, and centered on the page

###### `description` - optional
A text that is italic, aligned left and fits right under the header

##### Text
You can of course add non-fancy text anywhere
```JSON
{
	"id": 2,
	"description": "Another element, this time only with text"
}
```


###### `id` - required
The `id` is required to be a **unique number** for every page item, as this identifies them later. The order is not important,
they just have to be unique.

###### `description` - required
A text that is italic, aligned left

##### Achievement
Adding achievements is no different
```JSON
{
	"id": 3,
	"achievement": "Carefully read the documentation",
	"mod": "Achievement Books"
}
```

###### `id` - required
The `id` is required to be a **unique number** for every page item, as this identifies them later. The order is not important,
they just have to be unique.

###### `achievement` - required
The achievement text. Keep it short and concise if you want to make it look good. #protip

###### `mod` - optional
An extra information for the player, if the achievement is targeting a specific mod. This shows up in brackets and in blue


#### Example
```JSON
{
	"itemName": "book_example",
	"bookName": "Achievement Book Demo",
	"color": "black",
	"craftingMaterial": "minecraft:cobblestone",
	"pages": [
		[
			{
				"id": 1,
				"header": "Welcome to the Achievement Book Demo2",
				"description": "This book is only here to show you the capabilities of this mod\n"
			},
			{
				"id": 2,
				"description": "You can define a texts, headers and achievements in the config/achievementbooks folder"
			}
		],
		[
			{
				"id": 3,
				"header": "Getting to know the config"
			},
			{
				"id": 5,
				"achievement": "Craft the Achievement Book Demo book",
				"mod": "vanilla"
			},
			{
				"id": 4,
				"achievement": "Check out the demo config file for the Achievement Books Mod",
				"mod": "Achievement Books"
			},
			{
				"id": 6,
				"achievement": "Add a new achievement to the Achievement Book Demo",
				"mod": "Achievement Books"
			}
		],
		[
			{
				"id": 7,
				"header": "Advanced Topics"
			},
			{
				"id": 8,
				"achievement": "Create another achievement book next to the Achievement Book Demo",
				"mod": "Achievement Books"
			},
			{
				"id": 9,
				"achievement": "Change the wording of achievement id 4 in the Achievement Book Demo",
				"mod": "Achievement Books"
			},
			{
				"id": 10,
				"achievement": "Reorder the achievements on page 2 of the Achievement Book Demo",
				"mod": "Achievement Books"
			}
		]
	]
}

```



>#### Acknowledgment
>This mod has been inspired by the Simple Achievements mod which has aided my journey into Minecraft, with Sky Factory.
>Upon building my own modpack, I needed a bit more flexibility, hence a "simple" book wasn't enough.


