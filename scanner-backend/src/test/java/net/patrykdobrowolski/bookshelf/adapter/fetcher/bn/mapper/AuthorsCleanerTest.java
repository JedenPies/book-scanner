package net.patrykdobrowolski.bookshelf.adapter.fetcher.bn.mapper;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

public class AuthorsCleanerTest {

    @ParameterizedTest
    @CsvSource(delimiter = ':', value = {
            "Doerrfeld, Cori Raczek, Zofia Grupa Wydawnicza Relacja:Grupa Wydawnicza Relacja Mamania",
            "Smilgin, Radosław Wydawnictwo Naukowe PWN:Wydawnictwo Naukowe PWN Wydawnictwo Naukowe PWN",
            "Kownacka, Maria (1894-1982) Heintze, Jerzy (1922-1995) Rychlicki, Zbigniew (1922-1989) Młodzieżowa Agencja Wydawnicza:Młodzieżowa Agencja Wydawnicza Młodzieżowa Agencja Wydawnicza",
            "Długosz, Dominika (dziennikarka) Czerwone i Czarne:Czerwone i Czarne Czerwone i Czarne",
            "Galewska-Kustra, Marta (1979- ) Kłos, Joanna Wydawnictwo \"Nasza Księgarnia\" Galewska-Kustra, Marta.:Wydawnictwo \"Nasza Księgarnia\" Nasza Księgarnia",
            "Martin, Robert C. (1952- ) Gonera, Paweł Helion:Helion Helion",
            "Kołak-Rodis, Joanna Kost, Alicja Wydawnictwo Book Ojciec:Wydawnictwo Book Ojciec Book Ojciec sp. z o.o.",
            "Freeman, Eric (1965- ) Robson, Elisabeth (programistka) Kowalczyk, Grzegorz (informatyk) Rajca, Piotr (1970- ) Koronkiewicz, Paweł (1973- ) Helion:Helion Helion",
            "Hartley-Brewer, Elizabeth Waliś, Robert (1979- ) Wydawnictwo K.E. Liber, Krzysztof Kołakowski:Wydawnictwo K.E. Liber, Krzysztof Kołakowski Liber",
            "Shapira, Gwen Palino, Todd Petty, Krit Lachowski, Lech Sivaram, Rajini Helion:Helion Helion",
            "Głowińska, Anita Media Rodzina Głowińska, Anita.:Media Rodzina Media Rodzina",
            "Broda, Martyna Hyla, Karolina Grupa Wydawnicza Relacja:Grupa Wydawnicza Relacja Mamania",
            "Baranowski, Tadeusz (1945-2026) Wydawnictwo Ongrys:Wydawnictwo Ongrys Wydawnictwo Ongrys",
            "Baranowska, Anna Baranowski, Tadeusz (1945-2026) Wydawnictwo Ongrys:Wydawnictwo Ongrys Wydawnictwo Ongrys",
            "Baranowski, Tadeusz (1945-2026) Kultura Gniewu:Kultura Gniewu Krótkie Gatki",
            "Baranowski, Tadeusz (1945-2026) Siemiątkowska, Joanna (1950- ) Spanowicz, Aleksandra (1966- ) Wydawnictwo Ongrys:Wydawnictwo Ongrys Wydawnictwo Ongrys",
            "Baranowski, Tadeusz (1945-2026) Wydawnictwo Ongrys:Wydawnictwo Ongrys Ongrys",
            "Wawiłow, Danuta (1942-1999). Wawiłow, Danuta (1942-1999). Porazińska, Janina (1888-1971). Papuzińska, Joanna (1939- ). Grabowski, Sławomir (1942- ). Grabowski, Sławomir (1942- ). Grabowski, Sławomir (1942- ). Bechlerowa, Helena (1908-1995). Gaudasińska, Elżbieta (1943- ) Karwowska-Wnuczak, Julitta (1935- ) Lutczyn, Edward (1947- ) Nejman, Marek (1945- ) Orłowska-Gabryś, Maria (1925-1988) Przymanowska-Boniuk, Danuta (1946- ) Bechlerowa, Helena (1908-1995). Wajs, Joanna (1979- ) Stylo-Ginter, Anna (1934- ) Bechlerowa, Helena (1908-1995). Witwicki, Zdzisław (1921-2019) Rychlicki, Zbigniew (1922-1989) Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" Wydawnictwo \"Nasza Księgarnia\"",
            "Holender, Małgorzata (1948- ) Kuczyński, Maciej (1929-2019) Wróblewski, Jerzy (1941-1991) Wydawnictwo Ongrys:Wydawnictwo Ongrys Wydawnictwo Ongrys",
            "Roman, Adam (informatyk) Roman, Adam (informatyk) Stapp, Lucjan Helion:Helion Helion",
            "Flowers, Luke Eliopulos, Nick. Eliopulos, Nick. Hikiert-Bereza, Anna Eliopulos, Nick. Eliopulos, Nick. Eliopulos, Nick. HarperCollins Polska:HarperCollins Polska HarperCollins Polska Sp. z o.o.",
            "Kruusval, Catarina (1951- ) Skalska, Katarzyna Wydawnictwo Zakamarki:Wydawnictwo Zakamarki Wydawnictwo Zakamarki",
            "Dragan, Andrzej (1978- ) Wydawnictwo Otwarte:Wydawnictwo Otwarte Otwarte",
            "Ferrón, Sheddad Kaid-Sala Kaszorek, Katarzyna (1989- ) Altarriba, Eduard Wydawnictwo Adamada:Wydawnictwo Adamada Wydawnictwo Adamada",
            "Piątkowska, Renata (1958- ) Kalenik, Iwona (1978- ) Wydawnictwo Bis:Wydawnictwo Bis Wydawnictwo Bis",
            "Jelley, Craig Hikiert-Bereza, Anna Marsh, Ryan HarperCollins Polska:HarperCollins Polska HarperCollins Polska",
            "Piątkowska, Renata (1958- ) Kalenik, Iwona (1978- ):bis",
            "Newman, Sam Meryk, Radosław Helion:Helion Wydawnictwo Helion",
            "Wojtkowiak-Skóra, Patrycja Matz, Agnieszka Wydawnictwo Trefl Wojtkowiak-Skóra, Patrycja.:Wydawnictwo Trefl Wydawnictwo Trefl Sp. z o. o.",
            "Kozłowska, Katarzyna (pedagożka) Schoett, Marianna Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" Wydawnictwo \"Nasza Księgarnia\"",
            "Kozłowska, Katarzyna (pedagożka) Schoett, Marianna Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" [Wydawnictwo \"Nasza Księgarnia\"]",
            "Kownacka, Maria (1894-1982) Rychlicki, Zbigniew (1922-1989) Heintze, Jerzy (1922-1995) Młodzieżowa Agencja Wydawnicza Kownacka, Maria (1894-1982).:Młodzieżowa Agencja Wydawnicza Młodzieżowa Agencja Wydawnicza",
            "Sokołowski, Jarosław (1962- ) Górski, Artur (1964- ) Prószyński Media:Prószyński Media Prószyński Media",
            "Läckberg, Camilla (1974- ) Sawicka, Inga Wydawnictwo Czarna Owca Läckberg, Camilla (1974- ).:Wydawnictwo Czarna Owca Wydawnictwo Czarna Owca",
            "Żemła, Edyta Czerwone i Czarne:Czerwone i Czarne Czerwone i Czarne",
            "Benedictus, David (1938-2023) Burgess, Mark Gajos, Janusz (1939- ) Milne, A. A. (1882-1956) Rusinek, Michał (1972- ) Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" Wydawnictwo \"Nasza Księgarnia\"",
            "Marsh, Ryan Hikiert-Bereza, Anna HarperCollins Polska:HarperCollins Polska Harperkids",
            "Piątkowska, Renata (1958- ) Kalenik, Iwona (1978- ) Wydawnictwo Bis:Wydawnictwo Bis Bis",
            "McBrien, Thomas Hikiert-Bereza, Anna Marsh, Ryan HarperCollins Polska:HarperCollins Polska HarperCollins Polska",
            "Hikiert-Bereza, Anna Mojang HarperCollins Polska:HarperCollins Polska Harper Collins Polska sp. z o.o.",
            "Milne, A. A. (1882-1956) Gajos, Janusz (1939- ) Shepard, Ernest H. (1879-1976) Tuwim, Irena (1898-1987) Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" Wydawnictwo \"Nasza Księgarnia\"",
            "McBrien, Thomas Klonowski, Tomasz Bieriezjanczuk, Kate HarperCollins Polska:HarperCollins Polska Harperkids",
            "Wechterowicz, Przemysław (1975- ) Dziubak, Emilia. Agencja Edytorska Ezop:Agencja Edytorska Ezop Wydawnictwo Ezop",
            "Andersen, Hans Christian (1805-1875) Bartczak, Weronika Supeł, Barbara Wydawnictwo Zielona Sowa:Wydawnictwo Zielona Sowa Zielona Sowa",
            "Hawking, Stephen (1942-2018) Krośniak, Marek (1955- ) Zysk i S-ka Wydawnictwo:Zysk i S-ka Wydawnictwo Zysk i S-ka Wydawnictwo",
            "Wechterowicz, Przemysław (1975- ) Dziubak, Emilia (1982- ) Agencja Edytorska Ezop:Agencja Edytorska Ezop Wydawnictwo Ezop",
            "Sarah, Linda Pietrzyk, Małgorzata (1954- ) Davies, Benji (1980- ) Sarah, Linda (1949- ) Społeczny Instytut Wydawniczy Znak:Społeczny Instytut Wydawniczy Znak Znak Emotikon",
            "Bątkiewicz-Brożek, Joanna (1976- ) Skrzypczak, Robert (1964- ) Wydawnictwo Esprit:Wydawnictwo Esprit Wydawnictwo Esprit",
            "Davies, Benji (1980- ) Pietrzyk, Małgorzata (1954- ) Społeczny Instytut Wydawniczy Znak:Społeczny Instytut Wydawniczy Znak Znak",
            "Bruni, Amelia Tyszka, Wojciech De Agostini Polska:De Agostini Polska De Agostini Polska",
            "Richardson, Chris Rogulski, Mariusz Rogulska, Magdalena (informatyk) Wydawnictwo Naukowe PWN:Wydawnictwo Naukowe PWN PWN",
            "Leśniewski, Michał (1978- ) Dramczyk, Bartłomiej Łazowski, Maciej (1985- ) Egmont Polska:Egmont Polska Wydawnictwo Egmont Polska Sp. z o.o.",
            "Woźniak, Dariusz (programista) Helion:Helion Helion",
            "Kotarski, Radosław (1986- ) Altenberg:Altenberg Altenberg",
            "Martin, Robert C. (1952- ) Moch, Wojciech Helion:Helion Helion",
            "Milne, A. A. (1882-1956) Gajos, Janusz (1939- ) Shepard, Ernest H. (1879-1976) Tuwim, Irena (1898-1987) Wydawnictwo \"Nasza Księgarnia\":Wydawnictwo \"Nasza Księgarnia\" Wydawnictwo Nasza Księgarnia",
            "Dziubka, Kamil Czerwone i Czarne:Czerwone i Czarne Czerwone i Czarne",
            "Sokołowski, Jarosław (1962- ) Górski, Artur (1964- ) Prószyński Media:Prószyński Media Prószyński Media",
            "Evans, Eric (1962- ) Szpoton, Rafał Helion:Helion Wydawnictwo Helion"
    })
    void dumpCleanedAuthorsWithPublisher(String rawAuthors, String publisher) {
        List<String> authors = AuthorsCleaner.cleanAndExtract(rawAuthors, publisher);

        System.out.println("INPUT AUTHORS:   " + rawAuthors);
        System.out.println("INPUT PUBLISHER: " + publisher);
        System.out.println("OUTPUT:          " + authors);
        System.out.println("-".repeat(80));
    }
}

