package dmo.client.test {
import dmo.client.domain.AnimeLightDto;
import dmo.client.domain.AnimeTitleDto;
import dmo.client.domain.PageDtoOfAnimeLightDto;
import dmo.client.mapper.Mapper;

import org.apache.royale.test.Assert;

public class MappingUtilTest {
    public function MappingUtilTest() {
    }

    [Test]
    public function testAnimeListResponseConversion():void {
        const mapper:Mapper = new Mapper(PageDtoOfAnimeLightDto);
        const parsed:Object = mapper.fromDto(ANIME_LIST_RESPONSE_JSON);

        Assert.assertTrue(parsed is PageDtoOfAnimeLightDto);
        const page:PageDtoOfAnimeLightDto = parsed as PageDtoOfAnimeLightDto;

        Assert.assertEquals(page.number, 0);
        Assert.assertEquals(page.size, 10);
        Assert.assertEquals(page.numberOfElements, 10);
        Assert.assertEquals(page.totalPages, 1271);
        Assert.assertEquals(page.totalElements, 12706);

        Assert.assertEquals(page.content.length, 10, "Content Length");

        for (var i:int = 0; i < page.content.length; i++) {
            const animeObj:Object = page.content[i];
            Assert.assertTrue(animeObj is AnimeLightDto);

            const animeLight:AnimeLightDto = animeObj as AnimeLightDto;

            Assert.assertEquals(animeLight.id, i + 1);
            Assert.assertNotNull(animeLight.type);
            Assert.assertNotNull(animeLight.titles);

            for (var j:int = 0; j < animeLight.titles; j++) {
                const titleObj:Object = animeLight.titles[i];
                Assert.assertTrue(titleObj is AnimeTitleDto);

                const title:AnimeTitleDto = titleObj as AnimeTitleDto;

                Assert.assertNotNull(title.type);
                Assert.assertNotNull(title.lang);
                Assert.assertNotNull(title.text);
            }
        }
    }

    private static const ANIME_LIST_RESPONSE_JSON:Object = {
        "number": 0,
        "size": 10,
        "numberOfElements": 10,
        "totalPages": 1271,
        "totalElements": 12706,
        "content": [{
            "id": 1,
            "titles": [{"type": "SYNONYM", "lang": "ko", "text": "성계의 문장"}, {
                "type": "SHORT",
                "lang": "en",
                "text": "CotS"
            }, {"type": "SYNONYM", "lang": "zh-Hans", "text": "星界之纹章"}, {
                "type": "SYNONYM",
                "lang": "cs",
                "text": "Hvězdný erb"
            }, {"type": "SHORT", "lang": "x-jat", "text": "SnM"}, {
                "type": "OFFICIAL",
                "lang": "ja",
                "text": "星界の紋章"
            }, {"type": "OFFICIAL", "lang": "en", "text": "Crest of the Stars"}, {
                "type": "OFFICIAL",
                "lang": "fr",
                "text": "Crest of the Stars"
            }, {"type": "OFFICIAL", "lang": "pl", "text": "Crest of the Stars"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Seikai no Monshou"
            }],
            "type": "UNKNOWN"
        }, {
            "id": 2,
            "titles": [{"type": "SHORT", "lang": "x-jat", "text": "3x3"}, {
                "type": "OFFICIAL",
                "lang": "en",
                "text": "3x3 Eyes"
            }, {"type": "OFFICIAL", "lang": "es", "text": "3x3 Ojos [1-4]"}, {
                "type": "OFFICIAL",
                "lang": "it",
                "text": "3x3 Occhi"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "3×3 EYES"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "3x3 Eyes"
            }, {"type": "SYNONYM", "lang": "x-jat", "text": "Southern Eyes"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "3x3 Eyes - Immortals"
            }, {"type": "SYNONYM", "lang": "zh-x-nan", "text": "三隻眼"}, {
                "type": "OFFICIAL",
                "lang": "fr",
                "text": "3x3 Eyes"
            }, {"type": "SYNONYM", "lang": "ru", "text": "3x3 глаза"}, {
                "type": "SYNONYM",
                "lang": "ja",
                "text": "サザンアイズ"
            }, {"type": "OFFICIAL", "lang": "cs", "text": "3x3 Oči"}, {
                "type": "OFFICIAL",
                "lang": "de",
                "text": "3x3 Augen (OVA 1)"
            }, {"type": "SYNONYM", "lang": "x-jat", "text": "Sazan Eyes"}, {
                "type": "OFFICIAL",
                "lang": "sv",
                "text": "3x3 Ögon"
            }, {"type": "OFFICIAL", "lang": "ca", "text": "3x3 Ulls"}],
            "type": "UNKNOWN"
        }, {
            "id": 3,
            "titles": [{
                "type": "OFFICIAL",
                "lang": "it",
                "text": "3x3 Occhi - La Leggenda del Demone Divino"
            }, {"type": "SYNONYM", "lang": "x-jat", "text": "Shin 3x3 Eyes"}, {
                "type": "OFFICIAL",
                "lang": "ja",
                "text": "3×3 EYES -聖魔伝説-"
            }, {"type": "SYNONYM", "lang": "ru", "text": "3х3 глаза: Сказание Сэймы"}, {
                "type": "OFFICIAL",
                "lang": "en",
                "text": "3x3 Eyes: Legend of the Divine Demon"
            }, {"type": "MAIN", "lang": "x-jat", "text": "3x3 Eyes: Seima Densetsu"}, {
                "type": "OFFICIAL",
                "lang": "fr",
                "text": "3x3 Eyes - La légende du démon divin"
            }, {"type": "OFFICIAL", "lang": "pl", "text": "3*3 Oczy"}, {
                "type": "OFFICIAL",
                "lang": "de",
                "text": "3x3 Augen (OVA 2)"
            }, {"type": "SYNONYM", "lang": "sv", "text": "3x3 Ögon (1995)"}, {
                "type": "OFFICIAL",
                "lang": "es",
                "text": "3x3 Ojos [5-7]"
            }],
            "type": "UNKNOWN"
        }, {
            "id": 4,
            "titles": [{"type": "SYNONYM", "lang": "en", "text": "Battle Flag of the Stars"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Seikai no Senki"
            }, {"type": "OFFICIAL", "lang": "en", "text": "Banner of the Stars"}, {
                "type": "SHORT",
                "lang": "en",
                "text": "BFotS"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "星界の戦旗"}, {
                "type": "SHORT",
                "lang": "en",
                "text": "BotS1"
            }, {"type": "SHORT", "lang": "x-jat", "text": "SnS"}, {
                "type": "SHORT",
                "lang": "en",
                "text": "BotS"
            }, {"type": "SYNONYM", "lang": "zh-Hans", "text": "星界的战旗"}, {
                "type": "OFFICIAL",
                "lang": "ko",
                "text": "성계의 전기"
            }, {"type": "SHORT", "lang": "x-jat", "text": "SnS1"}],
            "type": "UNKNOWN"
        }, {
            "id": 5,
            "titles": [{"type": "SYNONYM", "lang": "ko", "text": "성계의 전기 II"}, {
                "type": "OFFICIAL",
                "lang": "en",
                "text": "Banner of the Stars II"
            }, {"type": "SHORT", "lang": "en", "text": "BFotS2"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Banner of the Stars 2"
            }, {"type": "SHORT", "lang": "en", "text": "BotS2"}, {
                "type": "SYNONYM",
                "lang": "x-jat",
                "text": "Seikai no Senki 2"
            }, {"type": "MAIN", "lang": "x-jat", "text": "Seikai no Senki II"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Battle Flag of the Stars 2"
            }, {"type": "SYNONYM", "lang": "zh-Hans", "text": "星界的战旗2"}, {
                "type": "SHORT",
                "lang": "x-jat",
                "text": "SnS2"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "星界の戦旗II"}],
            "type": "UNKNOWN"
        }, {
            "id": 6,
            "titles": [{"type": "OFFICIAL", "lang": "en", "text": "Crest of the Stars: Birth"}, {
                "type": "SYNONYM",
                "lang": "ko",
                "text": "성계의 단장"
            }, {"type": "SYNONYM", "lang": "ko", "text": "성계의 단장: 탄생"}, {
                "type": "OFFICIAL",
                "lang": "ja",
                "text": "星界の断章 誕生"
            }, {"type": "SHORT", "lang": "x-jat", "text": "SnD"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Crest of the Stars: Fragment Birth of Stars"
            }, {"type": "SYNONYM", "lang": "en", "text": "Lost Chapter of the Stars"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Seikai no Danshou: Tanjou"
            }, {"type": "SYNONYM", "lang": "x-jat", "text": "Seikai no Monshou Birth"}, {
                "type": "SYNONYM",
                "lang": "zh-Hans",
                "text": "星界的断章"
            }],
            "type": "UNKNOWN"
        }, {
            "id": 7,
            "titles": [{"type": "OFFICIAL", "lang": "pt-BR", "text": "Princesa Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "sk",
                "text": "Princezná Mononoke"
            }, {"type": "OFFICIAL", "lang": "cs", "text": "Princezna Mononoke"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Mononoke-hime"
            }, {"type": "OFFICIAL", "lang": "hr", "text": "Princeza Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "hu",
                "text": "A vadon hercegnője"
            }, {"type": "OFFICIAL", "lang": "ru", "text": "Принцесса Мононоке"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Mononoke Princess"
            }, {"type": "OFFICIAL", "lang": "lt", "text": "Princesė Mononokė"}, {
                "type": "OFFICIAL",
                "lang": "bg",
                "text": "Принцеса Мононоке"
            }, {"type": "OFFICIAL", "lang": "pt", "text": "A Princesa Mononoke"}, {
                "type": "SYNONYM",
                "lang": "ko",
                "text": "원령공주"
            }, {"type": "OFFICIAL", "lang": "it", "text": "La Principessa Mononoke"}, {
                "type": "SYNONYM",
                "lang": "vi",
                "text": "Công chúa sói"
            }, {"type": "OFFICIAL", "lang": "lv", "text": "Princese Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "ro",
                "text": "Prinţesa Mononoke"
            }, {"type": "SYNONYM", "lang": "zh-Hans", "text": "幽灵公主"}, {
                "type": "OFFICIAL",
                "lang": "el",
                "text": "Πριγκίπισσα Μονονόκε"
            }, {"type": "SYNONYM", "lang": "ar", "text": "الأميرة مونونوكيه"}, {
                "type": "SYNONYM",
                "lang": "ru",
                "text": "Принцесса Мононокэ"
            }, {"type": "OFFICIAL", "lang": "pl", "text": "Księżniczka Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "tr",
                "text": "Prenses Mononoke"
            }, {"type": "OFFICIAL", "lang": "fi", "text": "Prinsessa Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "da",
                "text": "Prinsesse Mononoke"
            }, {"type": "OFFICIAL", "lang": "en", "text": "Princess Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "es",
                "text": "La Princesa Mononoke"
            }, {"type": "OFFICIAL", "lang": "fr", "text": "Princesse Mononoké"}, {
                "type": "OFFICIAL",
                "lang": "ar",
                "text": "الأميرة مونونوكي"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "もののけ姫"}, {
                "type": "OFFICIAL",
                "lang": "et",
                "text": "Printsess Mononoke"
            }, {"type": "OFFICIAL", "lang": "de", "text": "Prinzessin Mononoke"}, {
                "type": "OFFICIAL",
                "lang": "fa",
                "text": "شاهزاده مونونوکه"
            }, {"type": "OFFICIAL", "lang": "zh-Hans", "text": "魔法公主"}, {
                "type": "OFFICIAL",
                "lang": "zh-Hant",
                "text": "幽靈公主"
            }, {"type": "SYNONYM", "lang": "sr", "text": "Принцеза Мононоке"}, {
                "type": "OFFICIAL",
                "lang": "ko",
                "text": "모노노케 히메"
            }, {"type": "OFFICIAL", "lang": "he", "text": "הנסיכה מונונוקי"}, {
                "type": "SYNONYM",
                "lang": "uk",
                "text": "Принцеса Мононоке"
            }, {"type": "SYNONYM", "lang": "uk", "text": "Принцеса-мононоке"}, {
                "type": "OFFICIAL",
                "lang": "sl",
                "text": "Princesa Mononoke"
            }, {"type": "OFFICIAL", "lang": "mn", "text": "Мононокэ Гүнж"}],
            "type": "UNKNOWN"
        }, {
            "id": 8,
            "titles": [{
                "type": "OFFICIAL",
                "lang": "en",
                "text": "Aquarian Age: Sign for Evolution"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "アクエリアンエイジ Sign for Evolution"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Aquarian Age: Sign for Evolution"
            }, {"type": "OFFICIAL", "lang": "de", "text": "Aquarian Age: Sign for Evolution"}, {
                "type": "OFFICIAL",
                "lang": "fr",
                "text": "L`âge du Verseau - Signe d`évolution"
            }],
            "type": "UNKNOWN"
        }, {
            "id": 9,
            "titles": [{"type": "SHORT", "lang": "en", "text": "TSFS"}, {
                "type": "OFFICIAL",
                "lang": "ja",
                "text": "ちっちゃな雪使いシュガー"
            }, {"type": "OFFICIAL", "lang": "en", "text": "A Little Snow Fairy Sugar"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Sugar: A Little Snow Fairy"
            }, {"type": "SHORT", "lang": "en", "text": "ALSFS"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Tiny Snow Fairy Sugar"
            }, {"type": "SYNONYM", "lang": "en", "text": "Tiny Little Snow Fairy Sugar"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Chiccha na Yuki Tsukai Sugar"
            }, {"type": "SYNONYM", "lang": "pt-BR", "text": "Fadinha de Neve Sugar"}, {
                "type": "SYNONYM",
                "lang": "bg",
                "text": "Малката снежна фея Шугър"
            }, {"type": "SYNONYM", "lang": "x-jat", "text": "Chiccha na Yukitsukai Sugar"}, {
                "type": "SHORT",
                "lang": "x-jat",
                "text": "sugar"
            }],
            "type": "UNKNOWN"
        }, {
            "id": 10,
            "titles": [{"type": "OFFICIAL", "lang": "en", "text": "Bastard!!"}, {
                "type": "SYNONYM",
                "lang": "ru",
                "text": "Ублюдок!! Сокрушитель Тьмы"
            }, {"type": "SYNONYM", "lang": "ja", "text": "バスタード!! 暗黒の破壊神"}, {
                "type": "SYNONYM",
                "lang": "it",
                "text": "Bastard!! L`oscuro distruttore"
            }, {"type": "OFFICIAL", "lang": "ja", "text": "BASTARD!! 暗黒の破壊神"}, {
                "type": "SYNONYM",
                "lang": "en",
                "text": "Bastard! Destroyer of Darkness"
            }, {"type": "OFFICIAL", "lang": "it", "text": "Bastard!!"}, {
                "type": "MAIN",
                "lang": "x-jat",
                "text": "Bastard!! Ankoku no Hakai Shin"
            }, {"type": "SYNONYM", "lang": "zh-Hans", "text": "暗黑破坏神"}],
            "type": "UNKNOWN"
        }]
    };
}
}
