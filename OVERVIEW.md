# Oversikt:
##Resurser:
De fleste ressurser er hentet fra nettet forutenom alle smmålyder som er generert ved hjelp
av et open-source programm:
###Sprites:
* <a href='http://opengameart.org/content/10-basic-rpg-enemies'>Monster</a> no changes
* <a href='http://opengameart.org/content/business-of-rage-characeter-beatemup-2d'>Player</a> some resizeing an cut-outs
* <a href='http://opengameart.org/content/gem-icons'>Diamond</a> no changes
* <a href='http://opengameart.org/content/metalstone-textures'>Wall</a> heavy resize
* <a href='http://opengameart.org/content/hand-painted-texture-sandstone'>Sand</a> heavy resize
* <a href='http://opengameart.org/content/forest-scene-items-animated-slimeexp-orbs'>Rock</a> small resize
* <a href='http://opengameart.org/content/basic-map-32x32-by-silver-iv'>Door</a> no changes

###Backgrounds:
* <a href='http://opengameart.org/content/seamless-cave-background'>Blue cave</a> no changes
* <a href='http://opengameart.org/content/backgrounds-0'>All Other</a> no changes

###Sound:
* Not implemented yet

##Forståelse av programmet før programmering:
Programmet var meget vanskelig å sette seg inn i til å starte med. Førte egentlig til at jeg selv
måtte leke rundt for å prøve på de forskjellige filene. Men jeg merket meg en del forsjellige ting:
1) Når vi starter i inf101.v17 ser vi at den inneholder flere pakker
    * vi trenger egentlig ikke å se for mye innom disse da de bare inneholder klasser vi
    bare bruker i oppgaven, og disse burde det være kontroll på ifra tidligere semmoppg
    * det eneste er .boulderdash som faktisk er hele semmoppgaven
2) Boulderdash mappen inneholder mange underpakker og klasser. Hvis vi åpnar alle etter tur
vil det tydelig ta sin tid. Her har vi litt flaks for det er mulig å generere en javadoc.
Så vi kan lett lese igjennom og forstå at:
    * Main klassen er der det hele starter
    * IllegalMoveException ser ut som en egen exception for feil inni spillet
    * Position er en klasse for å holde styr på orientering i spillet
    * Direction er e Enum med rettning som brukes i bla Position.
    * Maps er en pakke med filer for å håndtere gridet
        * Hvor BDMap er en grid klassen som håndterer de fleste objektene
        * Og MapReader er en klasse som leser fra fil og danner en grid?
    * gui er selvsakt en pakke for å håndtere det grafiske brukergrensesnittet så her er nok
    en del å sette seg inn i er jeg redd.
    * og tilsist bdobjects pakken som tydelig inneholder alle klassene som danner objektene til selve
    spillet.
3) Dersom vi nå ser tydeligere inn i inf101.v17.boulderdash.bdobjects er det en rar navngivning på
klassene til å starte med, heldigvis gir det mye mer mening i IntelliJ da det med en gang grafisk
forteller oss att dette ikke bare er klassiske klasser:
    * IBDObject, IBDMovingObject og IBDKillable er interface som ser ut til å være viktige for alle objekter
    * AbstractBDFallingObject, AbstractBDKillingObject, AbstractBDMovingObject, AbstractBDObject ser også
    ut til å være viktige og er der muligens for å sammle alle objekter av sin kategori
    * Tilsist har vi alle elementene som skal brukes i selve spillet:
        * BDBug         Håndterer monstre i spiller
        * BDDiamond     Håndterer diamanter (extender også falling, så kan trolig falle)
        * BDEmpty       Håndterer "ingenting" / tomme celler
        * BDPlayer      Håndterer Spilleren
        * BDSand        Håndterer knuselig sand
        * BDWall        Håndterer Uknuselige vegger
        
##Forståelse av programmet ETTER programmering:
1) Viser seg at de fleste av hypotesene er korrekte, men var vanskelig å sette seg inn i allt.
nå har det seg slik at interfacet IBDObject var det viktigste som hånterte ALLE andre objekter
som skulle brukest i spillet. Men oppgaven har helt klart gitt meg mye bedre innsikt i interface
og så gir endelig IBD skikkelig mening. I = interface, BD = boulderdash.