const blockstate = `{
  "variants": {
    "facing=east,half=lower,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_bottom"
    },
    "facing=east,half=lower,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 90
    },
    "facing=east,half=lower,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_bottom_hinge"
    },
    "facing=east,half=lower,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 270
    },
    "facing=east,half=upper,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_top"
    },
    "facing=east,half=upper,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 90
    },
    "facing=east,half=upper,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_top_hinge"
    },
    "facing=east,half=upper,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_top",
      "y": 270
    },
    "facing=north,half=lower,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 270
    },
    "facing=north,half=lower,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_bottom_hinge"
    },
    "facing=north,half=lower,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 270
    },
    "facing=north,half=lower,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 180
    },
    "facing=north,half=upper,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_top",
      "y": 270
    },
    "facing=north,half=upper,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_top_hinge"
    },
    "facing=north,half=upper,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 270
    },
    "facing=north,half=upper,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_top",
      "y": 180
    },
    "facing=south,half=lower,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 90
    },
    "facing=south,half=lower,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 180
    },
    "facing=south,half=lower,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 90
    },
    "facing=south,half=lower,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_bottom"
    },
    "facing=south,half=upper,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_top",
      "y": 90
    },
    "facing=south,half=upper,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 180
    },
    "facing=south,half=upper,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 90
    },
    "facing=south,half=upper,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_top"
    },
    "facing=west,half=lower,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 180
    },
    "facing=west,half=lower,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 270
    },
    "facing=west,half=lower,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_bottom_hinge",
      "y": 180
    },
    "facing=west,half=lower,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_bottom",
      "y": 90
    },
    "facing=west,half=upper,hinge=left,open=false": {
      "model": "minecraft:block/acacia_door_top",
      "y": 180
    },
    "facing=west,half=upper,hinge=left,open=true": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 270
    },
    "facing=west,half=upper,hinge=right,open=false": {
      "model": "minecraft:block/acacia_door_top_hinge",
      "y": 180
    },
    "facing=west,half=upper,hinge=right,open=true": {
      "model": "minecraft:block/acacia_door_top",
      "y": 90
    }
  }
}`;

const modelBottom = `{
  "parent": "minecraft:block/door_bottom",
  "textures": {
    "top": "minecraft:block/acacia_door_top",
    "bottom": "minecraft:block/acacia_door_bottom"
  }
}`;

const modelBottomHinge = `{
  "parent": "minecraft:block/door_bottom_rh",
  "textures": {
    "top": "minecraft:block/acacia_door_top",
    "bottom": "minecraft:block/acacia_door_bottom"
  }
}`;

const modelTop = `{
  "parent": "minecraft:block/door_top",
  "textures": {
    "top": "minecraft:block/acacia_door_top",
    "bottom": "minecraft:block/acacia_door_bottom"
  }
}`;

const modelTopHinge = `{
  "parent": "minecraft:block/door_top_rh",
  "textures": {
    "top": "minecraft:block/acacia_door_top",
    "bottom": "minecraft:block/acacia_door_bottom"
  }
}`;

const itemModel = `{
  "parent": "item/generated",
  "textures": {
    "layer0": "the_vault:item/gem_painite"
  }
}`;

const gems = [
  "alexandrite",
  "benitoite",
  "larimar",
  "black_opal",
  "painite",
  "iskallium",
  "renium",
  "gorginite",
  "sparkletine",
  "wutodie",
];

const gemNames = {
  alexandrite: "Alexandrite",
  benitoite: "Benitoite",
  larimar: "Larimar",
  black_opal: "Black Opal",
  painite: "Painite",
  iskallium: "Iskallium",
  renium: "Renium",
  gorginite: "Gorginite",
  sparkletine: "Sparkletine",
  wutodie: "Wutodie",
};

const fs = require("fs");
const pathLib = require("path");

function writeFile(path, data) {
  fs.mkdirSync(pathLib.dirname(path), { recursive: true });
  fs.writeFile(path, data, { flag: "w" }, (err) => {
    if (err) console.log(err);
    else console.log("Wrote", path);
  });
}

for (let gem of gems) {
  console.log(`"item.the_vault.key_${gem}": "${gemNames[gem]} Key",`);

  // writeFile(
  //   `./out/models/item/key_${gem}.json`,
  //   itemModel.replace(
  //     /the_vault:item\/gem_painite/gi,
  //     `the_vault:item/key_${gem}`
  //   )
  // );

  // writeFile(
  //   `./out/blockstates/door_${gem}.json`,
  //   blockstate.replace(
  //     /minecraft:block\/acacia_door/gi,
  //     `the_vault:block/door_${gem}`
  //   )
  // );

  // writeFile(
  //   `./out/models/block/door_${gem}_bottom.json`,
  //   modelBottom.replace(
  //     /minecraft:block\/acacia_door/gi,
  //     `the_vault:block/door_${gem}`
  //   )
  // );

  // writeFile(
  //   `./out/models/block/door_${gem}_bottom_hinge.json`,
  //   modelBottomHinge.replace(
  //     /minecraft:block\/acacia_door/gi,
  //     `the_vault:block/door_${gem}`
  //   )
  // );

  // writeFile(
  //   `./out/models/block/door_${gem}_top.json`,
  //   modelTop.replace(
  //     /minecraft:block\/acacia_door/gi,
  //     `the_vault:block/door_${gem}`
  //   )
  // );

  // writeFile(
  //   `./out/models/block/door_${gem}_top_hinge.json`,
  //   modelTopHinge.replace(
  //     /minecraft:block\/acacia_door/gi,
  //     `the_vault:block/door_${gem}`
  //   )
  // );
}
