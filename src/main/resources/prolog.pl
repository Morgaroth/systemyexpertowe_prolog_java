:- dynamic
    prawda/1,
    falsz/1,
    musiByc/1,
    musiMiec/1,
    jest/2,
    ma/2,
    propozycja/1,
    rasa/1,
    listaPropozycji/1,
    wypiszRozwiazanie/1.

rasa(jamnik).
rasa(pudel).
rasa(bernardyn).
rasa(buldog).

jest(jamnik, maly).
jest(jamnik, charakterny).
jest(jamnik, tropiacy).

jest(pudel, sredni).
jest(pudel, uzytkowy).

jest(bernardyn, olbrzymi).
jest(bernardyn, lagodny).
jest(bernardyn, pasterski).

ma(jamnik, 'krotka siersc').
ma(jamnik, 'krotkie lapy').
ma(pudel, 'krecona siersc').
ma(bernardyn, 'dluga siersc').

start :- listaPropozycji(L), nl, wypiszRozwiazanie(L).
wypiszRozwiazanie(L) :- L=[], write('Brak rozwiazan - kup sobie rybki'), nl.
wypiszRozwiazanie(L) :- length(L, Len), Len = 1, [El|_] = L, write('Moja propozycja to '), print(El), nl.
wypiszRozwiazanie(L) :- length(L, Len), Len > 1,  write('Moje propozycje to:'), nl, printlist(L), nl.
listaPropozycji(L) :- findall(X, (rasa(X), propozycja(X)), L).
listaMozliwosci(L) :- findall(X, (rasa(X), mozliwosc(X)), L).
propozycja(X) :- \+maWade(X), \+nieMaZalety(X).
mozliwosc(X) :- \+mozeMaWade(X), \+mozeNieMaZalety(X).
maWade(X) :- jest(X, Y), sprawdz(nieMozeByc(Y)).
maWade(X) :- ma(X, Y), sprawdz(nieMozeMiec(Y)).
nieMaZalety(X) :- \+jest(X, Y), sprawdz(musiByc(Y)).
nieMaZalety(X) :- \+ma(X, Y), sprawdz(musiMiec(Y)).
mozeMaWade(X) :- jest(X, Y), prawda(nieMozeByc(Y)).
mozeMaWade(X) :- ma(X, Y), prawda(nieMozeMiec(Y)).
mozeNieMaZalety(X) :- \+jest(X, Y), prawda(musiByc(Y)).
mozeNieMaZalety(X) :- \+ma(X, Y), prawda(musiMiec(Y)).

sprawdz(nieMozeByc(X)) :- nieMozeByc(X), assertz(prawda(nieMozeByc(X))).
sprawdz(musiByc(X)) :- musiByc(X), assertz(prawda(musiByc(X))).
sprawdz(nieMozeMiec(X)) :- nieMozeMiec(X), assertz(prawda(nieMozeMiec(X))).
sprawdz(musiMiec(X)) :- musiMiec(X), assertz(prawda(musiMiec(X))).

nieMozeByc(duzy) :- gdy('mieszkasz w miescie').
nieMozeByc(olbrzymi) :- prawda('mieszkasz w miescie').
nieMozeByc(olbrzymi) :- nieMozeByc(duzy).
nieMozeByc(maly) :- gdy('ma byc obronny').
nieMozeByc('niebezpieczna rasa') :- gdy('masz dzieci').
nieMozeByc('charakterny') :- gdy('masz dzieci').
nieMozeByc('duzy') :- prawda('masz auto'), gdy('masz male auto').
nieMozeByc(duzy) :- prawda('mieszkasz w miescie'), gdy('mieszkasz powyzej parteru').
nieMozeByc(tropiacy) :- gdy('posiadasz zwierzeta'), gdy('posiadasz male zwierzeta').
nieMozeByc(charakterny) :- prawda('posiadasz zwierzeta'), gdy('masz inne psy').
nieMozeByc(maly) :- prawda('pies ma pomagac czlowiekowi'), gdy('pies ma byc przewodnikiem dla osoby niewidomej').
nieMozeByc(kanapowy):- gdy('pies ma pomagac czlowiekowi').

musiByc(maly) :- \+gdy('masz auto').
musiByc(tropiacy) :- prawda('pies ma pomagac czlowiekowi'), gdy('pies ma wykrywac narkotyki').
musiByc(lagodny) :- prawda('pies ma pomagac czlowiekowi'), gdy('pies ma pomagac w rehabilitacji').
musiByc('lagodny') :- gdy('masz dzieci').

nieMozeMiec('krotkie lapy') :- gdy('chcesz chodzic z psem po gorach').
nieMozeMiec('krotka siersc') :- gdy('masz uczulenie na siersc').

musiMiec('krotka sierc') :- prawda('masz dzieci'), gdy('masz male dzieci').

gdy(X) :- prawda(X).
gdy(X) :- \+falsz(X), \+prawda(X), czy(X), read(Odp), (Odp = 't' -> assertz(prawda(X)); assertz(falsz(X)), fail).

printlist([X|List]) :-
        write(X),nl,
        printlist(List), true.

czy(X) :- nl, write('Mozliwe propozycje:'), nl, listaMozliwosci(L), \+printlist(L), nl, write('Czy '), write(X), write('? (t/n)'), nl.