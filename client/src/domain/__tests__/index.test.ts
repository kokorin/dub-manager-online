import assert from "assert";
import { AnimeTitleType, AnimeType } from "../index";

test("AnimeTitleType is comparable with string", () => {
    assert(AnimeTitleType.MAIN === "MAIN");
});

test("AnimeType is comparable with string", () => {
    assert(AnimeType.TV_SERIES === "TV_SERIES");
});
