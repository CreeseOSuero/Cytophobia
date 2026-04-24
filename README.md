# For PD8A

## The compilation of all maps are within the PD8A folder.

- res is a source package folder. When using the source code along with the assets directly, Set res as a source package folder
- res/assets0 contains the main menu assets
- while res/assets1 to res/assets10 contains the assets for each group

- All codes in PD8A are Menu-dependent in order to facilitate level switching
- <b>All codes in PD8A are made to be JAR compatible.</b>
  - This means that all image files are sourced through getClass().getResource()
  - All other files outside of jar are sourced through a Menu method called resolveFilePath()

<b>For groups not finished yet, please paste changes from your old source code into the new source code found in PD8A/src/Cytophobia</b>

Group folders are encouraged to be in this structure:

Under GroupN_LevelName, folders must be:
- assets[group#]/[same asset files]
- src/Cytophobia/[updated codes]
