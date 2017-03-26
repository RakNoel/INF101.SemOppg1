# Oversikt:
##Resurser:
De fleste ressurser er hentet fra nettet forutenom alle smmålyder som er generert ved hjelp
av et open-source programm:
###Sprites:
* <a href='http://opengameart.org/content/10-basic-rpg-enemies'>Monster</a>
    * cutout
    * Stephen "Redshrike" Challener, hosted by OpenGameArt.org
    * CC-BY 3.0 & OGA-BY 3.0
    
* <a href='http://opengameart.org/content/business-of-rage-characeter-beatemup-2d'>Player</a>
    * some resizeing and cut-outs
    * Public domain license (CC0)
    
* <a href='http://opengameart.org/content/gem-icons'>Diamond</a>
    * cutout
    * Clint Bellanger
    * CC-BY-SA 3.0
    
* <a href='http://opengameart.org/content/metalstone-textures'>Wall</a>
    * heavy resize
    * CC-BY 3.0 & CC-BY-SA 3.0
    * Spiney, No other info given

* <a href='http://opengameart.org/content/hand-painted-texture-sandstone'>Sand</a> 
    * heavy resize
    * CC-BY 3.0
    * KIIRA, No other info given

* <a href='http://opengameart.org/content/forest-scene-items-animated-slimeexp-orbs'>Rock</a>
    * small resize
    * Public domain (CC0)

* <a href='http://opengameart.org/content/basic-map-32x32-by-silver-iv'>Door</a>
    * cutout
    * CC-BY 3.0 & GPL 3.0 & GPL 2.0

* <a href='http://opengameart.org/content/slime-1'>Slime Spawner</a>
    * it's my uppload...
    * Public domain (CC0)

* <a href='http://opengameart.org/content/slime-1'>Slime Drop</a>
    * same as above...
    * Public domain (CC0)

###Backgrounds:
* <a href='http://opengameart.org/content/seamless-cave-background'>Blue cave</a>
    * no changes
    * PWL
    * Public domain (CC0)
    
* <a href='http://opengameart.org/content/backgrounds-0'>All Other</a>
    * no changes
    * Écrivain
    * Public domain (CC0)

###Sound:
* All sounds generated with dr.petters <a href='http://www.drpetter.se/project_sfxr.html'>project_sfxr!</a>

###Music:
* <a href='http://opengameart.org/content/spelunkers-anthem-cave-theme'>Background music</a>

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
        
##Beskriv systemet :
1) Interface danner jo en slags oppskrift på opprettelse av klasser. Men de bidrar også til å sammle de underliggende 
klassene som IBDObject som jør det mye lettere å returnere dem felles. Se spm2.

2) Arv spiller en stor rolle i systemet først og fremst for å kunne sende de forsjellige objektene enkelt rundt i 
systemet og enkelt caste dem. I tillegg til dette så sparer du mye coding da du bare kan hente funksjoner fra foreldre.
Oppå dette er også step arv bassert.

3) Abstrakte klasser blir en slags mellomting mellom Interface og klasser. Der interface bare er oppskrifter på metoder
en klasse MÅ inneholde imens en abstrakt klasse skal ekstemdes og kan ha både tomme metoder OG ferdige metoder.

4) Hovedlogikken vil eg si ligger i BDMap da du som oftest ser til denne etter metoder, og det er dette objektet som
dannes av main og sendes til GUI og holder alle objektene i en Grid

5) De abstrakte klassene gjør det lettere å samle objekter til testene først og fremst. Men du kan også si at de har 
til funksjon å være et bindeledd mellom klassene og Interface da de "tillbyr" noen ferdige metoder som du kan 
overskrive eller bruke som du vil.

6) For å legge til et nytt felt vil jeg tro vi må legge til et ny klasse slik som BDSand, implementere logikken og
oppdatere BDMap sin gridleser til å plassere ut det nye objektet.

7) Det ligger i stepp metoden til foreldren at en Diamant faller, AbstractBDFallingObject

##getPosition(IBDObject obj):
1) Vi kommer trolig til å trenge å hente posisjonen til et objekt. Enten for å teste om posisjon er korrekt, eller for
å vite hvilke objekter vi skal spørre om i get, eller hvilke objekter som ligger rundt oss.

Kunne vi lagret posisjonen til vert enkelt objekt i objektet selv? Trolig ja, men det ville vert lettere å gjøre en
feil perr objekttype.

2) Trolig er sammenhengen her selve objektene som legges til både i Grid og i hashmap.

3) Nei om possisjonen ble lagret inni objektet er objetet i seg selv kunne vi ikke like enkelt fått posisjonentil andre
objekter fra innsiden av et objekt, siden vi ikke kunne spørre owner.
        
##Forståelse av programmet ETTER programmering:
Viser seg at de fleste av hypotesene er korrekte, men var vanskelig å sette seg inn i allt.
nå har det seg slik at interfacet IBDObject var det viktigste som hånterte ALLE andre objekter
som skulle brukest i spillet. Men oppgaven har helt klart gitt meg mye bedre innsikt i interface
og så gir endelig IBD skikkelig mening. I = interface, BD = boulderdash.

##Retting av spørsmål ETTER programmering:
1) De har også en sammenheng da den ene er for ABSOLUTT ALLE underliggende objekter. Og de andre er for objekter 
som skal kunne drepest og en for alle bevegelige objekter.
   
5) Er også verdt å si at når du kaller på superstep går det opp til forelder og utfører det som står i dens step. 
Dette brukes bla i spilleren sin step når du endrer denne