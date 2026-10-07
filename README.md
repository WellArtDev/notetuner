# NoteTuner

Tune note-block music without guessing. NoteTuner watches what your
crosshair is pointing at, and the moment you look at a note block it
shows the block's currently tuned pitch right above the hotbar — like
`Note: F#2 (harp)`, including the instrument. Look away and the line
disappears, so it never gets in your way.

## Features

- **Pitch readout** — yellow text above the hotbar like
  `Note: F#2 (harp)` while you look at a note block
- **Chromatic scale** — covers all 25 pitches from F#1 up to F#3, plus
  the instrument name
- **Auto-clears** — the line vanishes the moment you look away
- **Zero config** — drop it in and play

## Install

1. Install [Fabric Loader](https://fabricmc.net/use/) 0.19.2+ for
   Minecraft 26.2
2. Install [Fabric API](https://modrinth.com/mod/fabric-api) for
   Minecraft 26.2
3. Drop `notetuner-1.0.0.jar` into your `mods` folder
4. Launch and play — no config needed

## Notes

- Client-side only. Works on any server (including vanilla).
- No mixins, no networking, nothing saved to disk.

## License

MIT — see [LICENSE](LICENSE).
