# OmniDungeon

[![Documentation](https://img.shields.io/badge/docs-Javadoc-blue)](https://trademarkthis.github.io/OmniDungeon/)

This is A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

**OmniDungeon** reimagines how traits and abilities are handled in RPGs. Instead of binding your race and class to fixed attributes, **items** become the core source of all traits. These traits include **modifiers** (like boosts to health, speed, or damage) and **actions** (such as attacking or casting spells), which are all provided by items. At the start, you may receive a set of items that define your race or class, but as you progress, you'll collect more items, and your true traits will emerge through what you *choose* to equip.

For example, in a traditional RPG, a “Zombie” race would come with predefined abilities, modifiers, and stats. In OmniDungeon, those traits come from an **item**, such as a Zombie Playing Card. This item would provide specific actions like "Zombie Bite" or "Zombie Lunge" and other modifiers, such as a **half-health** effect. You can equip this Zombie Card to any entity (including the player), and they too will gain the zombie traits. Effectively, this entity will now become part zombie.

Entities (including players) have three types of equipment slots: **Armor**, **Items**, and **Actions**. As the player, you can see these slots while other entities will automatically use them. When you equip an item or piece of armor, it may provide both modifiers (which *do not* take up any slots) and actions (which *do* take up slots in the **Action Bar**). For example, if each slot set has a limit (e.g., 8 slots), equipping an item that provides two actions will take up two of those slots. If all items provide two actions and both the **Item Bar** and **Action Bar** each have a size of 8, you'll only be able to equip 4 items. This forces you to prioritize items based on their overall **slot weight**.

Notably, your inventory will be infinite, so you'll never have to worry about running out of space for your items. If you're a collector, you can experiment with different combinations. You might discover that an item you found earlier is suddenly the perfect fit for your build.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
