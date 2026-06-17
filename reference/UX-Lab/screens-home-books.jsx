// screens-home-books.jsx — Home + Books, plus shared Header
(function(){
const { useState } = React;
const { AccountMenu, Carousel, BookCard, BottomNav, FEATURED, bookById, BOOKS, CAROUSEL } = window;
const MENU = {
  home:   [ {id:'books', icon:'book',  label:'All Books'}, {id:'stores', icon:'store', label:'Our Store'}, {id:'logout', icon:'logout', label:'Log Out'} ],
  books:  [ {id:'home',  icon:'home',  label:'Home'},      {id:'stores', icon:'store', label:'Our Store'}, {id:'logout', icon:'logout', label:'Log Out'} ],
  stores: [ {id:'home',  icon:'home',  label:'Home'},      {id:'books',  icon:'book',  label:'Books'},     {id:'logout', icon:'logout', label:'Log Out'} ],
};

function Header({ title, subtitle, kicker, menuKey, onNav }){
  return (
    <div style={{ display:'flex', alignItems:'flex-start', justifyContent:'space-between', padding:'64px 24px 0' }}>
      <div>
        {kicker && <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:11, letterSpacing:1.6,
          textTransform:'uppercase', color:'var(--caramel)', marginBottom:8 }}>{kicker}</div>}
        <div className="t-h2" style={{ fontSize:27 }}>{title}</div>
        {subtitle && <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:14, color:'var(--espresso-2)',
          opacity:.8, marginTop:7 }}>{subtitle}</div>}
      </div>
      <AccountMenu items={MENU[menuKey]} onPick={onNav} />
    </div>
  );
}

function HomeScreen({ user, onNav, onOpenBook }){
  const featured = FEATURED.map(bookById);
  return (
    <div style={{ position:'relative', height:'100%', background:'radial-gradient(125% 55% at 50% -8%, #FFF4E4 0%, #FFE9CF 46%)', overflow:'hidden', display:'flex', flexDirection:'column' }}>
      <div className="noscroll" style={{ flex:1, minHeight:0, overflowY:'auto', paddingBottom:130 }}>
        <Header kicker={'Welcome back'} title={'Hello, '+user+'!'} subtitle="Have a nice day"
          menuKey="home" onNav={onNav} />

        <div style={{ padding:'22px 24px 0' }}>
          <Carousel slides={CAROUSEL} onOpen={()=>onNav('stores')} />
        </div>

        <div style={{ display:'flex', alignItems:'baseline', justifyContent:'space-between', padding:'30px 24px 0' }}>
          <div className="t-h3" style={{ fontSize:19 }}>Featured Books</div>
          <button className="dc-link" onClick={()=>onNav('books')} style={{ fontSize:12 }}>See all →</button>
        </div>

        <div style={{ display:'grid', gridTemplateColumns:'repeat(2, max-content)', justifyContent:'space-between', rowGap:32,
          padding:'18px 24px 0' }}>
          {featured.map(b=> <BookCard key={b.id} book={b} w={146} onClick={()=>onOpenBook(b.id)} />)}
        </div>
      </div>
      <BottomNav active="home" onNav={onNav} />
    </div>
  );
}

function BooksScreen({ onNav, onOpenBook }){
  const [tab, setTab] = useState('Non-Fiction');
  const list = BOOKS.filter(b => b.cat === tab);
  return (
    <div style={{ position:'relative', height:'100%', background:'linear-gradient(180deg, #FFE9CF 0%, #FCE7CC 58%, #F6DCB8 100%)', overflow:'hidden', display:'flex', flexDirection:'column' }}>
      <div className="noscroll" style={{ flex:1, minHeight:0, overflowY:'auto', paddingBottom:130 }}>
        <Header kicker="The Collection" title="Our Library" subtitle="Timeless reads, carefully chosen"
          menuKey="books" onNav={onNav} />

        {/* segmented tabs */}
        <div style={{ margin:'22px 24px 0', background:'rgba(255,255,255,.55)', borderRadius:'var(--r-pill)',
          padding:5, display:'flex', position:'relative', boxShadow:'inset 0 1px 3px rgba(85,53,34,.08)' }}>
          <div style={{ position:'absolute', top:5, bottom:5, width:'calc(50% - 5px)', borderRadius:'var(--r-pill)',
            background:'var(--coffee)', boxShadow:'0 3px 8px rgba(85,53,34,.28)',
            transform:`translateX(${tab==='Non-Fiction'?0:'100%'})`,
            transition:'transform .32s cubic-bezier(.4,0,.2,1)' }}/>
          {['Non-Fiction','Fiction'].map(t=>(
            <button key={t} onClick={()=>setTab(t)} style={{ flex:1, zIndex:1, border:'none', background:'none',
              cursor:'pointer', padding:'10px 0', fontFamily:'var(--font-sans)', fontWeight:700, fontSize:13.5,
              letterSpacing:.4, color: tab===t?'#fff':'var(--espresso)', transition:'color .25s' }}>{t}</button>
          ))}
        </div>

        <div key={tab} style={{ display:'grid', gridTemplateColumns:'repeat(2, max-content)', justifyContent:'space-between', rowGap:30,
          padding:'24px 24px 0', animation:'dc-fade-up .32s ease both' }}>
          {list.map(b=> <BookCard key={b.id} book={b} w={146} showTag onClick={()=>onOpenBook(b.id)} />)}
        </div>
      </div>
      <BottomNav active="books" onNav={onNav} />
    </div>
  );
}

Object.assign(window, { Header, MENU, HomeScreen, BooksScreen });
})();
