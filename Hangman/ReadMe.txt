I implemented both offline mode and online mode hangman game.

For flexibility and extensibility, I decoupled hangmangame logic and UI. It is easy to change the UI and will not affect game logic.

At first, I want to make both offline mode and online mode in the same PlayActivity. But it is difficult, since we have to handle the asynchronous http request. So I let offline mode and online mode run in different activities.  

Since I have two phone interviews on 02/11/2015 and two onsite interviews on 02/12/2015 and 02/13/2015. I just implemented core features of hangman game. If I have time, I will give an improved version on 02/13/2015.