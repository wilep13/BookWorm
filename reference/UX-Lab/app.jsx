// app.jsx — router, device frame, transitions
(function(){
const { useState, useEffect } = React;
const { IOSDevice, AuthScreen, HomeScreen, BooksScreen, DetailScreen, StoresScreen, FEATURED } = window;

const LS = 'booworm.state.v1';
const load = ()=>{ try{ return JSON.parse(localStorage.getItem(LS)) || {}; }catch(e){ return {}; } };

function App(){
  const saved = load();
  const [screen, setScreen] = useState(saved.screen || 'auth');
  const [user, setUser]     = useState(saved.user || 'Reader');
  const [bookId, setBookId] = useState(saved.bookId || FEATURED[0]);
  const [from, setFrom]     = useState(saved.from || 'home');

  useEffect(()=>{
    localStorage.setItem(LS, JSON.stringify({ screen, user, bookId, from }));
  }, [screen, user, bookId, from]);

  const nav = (id)=>{
    if(id==='logout'){ setScreen('auth'); return; }
    setScreen(id);
  };
  const openBook = (id)=>{ if(screen!=='detail') setFrom(screen); setBookId(id); setScreen('detail'); };

  let view;
  if(screen==='auth')        view = <AuthScreen onLogin={(u)=>{ setUser(u||'Reader'); setScreen('home'); }} />;
  else if(screen==='home')   view = <HomeScreen user={user} onNav={nav} onOpenBook={openBook} />;
  else if(screen==='books')  view = <BooksScreen onNav={nav} onOpenBook={openBook} />;
  else if(screen==='stores') view = <StoresScreen onNav={nav} />;
  else if(screen==='detail') view = <DetailScreen bookId={bookId} onNav={nav} onBack={()=>setScreen(from)} />;

  return (
    <div style={{ minHeight:'100vh', width:'100%', display:'flex', flexDirection:'column', alignItems:'center',
      justifyContent:'center', gap:18, padding:'32px 16px' }}>
      <IOSDevice width={393} height={820}>
        <div key={screen} style={{ height:820, animation:'dc-fade-up .34s ease both' }}>
          {view}
        </div>
      </IOSDevice>
      <div style={{ display:'flex', alignItems:'center', gap:10, fontFamily:'var(--font-sans)', fontWeight:600,
        fontSize:12, color:'#9b8e7e' }}>
        <img src="assets/logo.png" alt="" style={{ height:20, width:'auto', opacity:.7 }}/>
        BOOWORM — interactive prototype ·
        <a href="Design System.html" style={{ color:'var(--caramel)', fontWeight:700, textDecoration:'none' }}>Design System ↗</a>
      </div>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
})();
