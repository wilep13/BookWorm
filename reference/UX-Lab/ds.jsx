// ds.jsx — Design System reference page (BOOWORM)
(function(){
const { useState } = React;
const { Logo, BookCard, BookCover, TextField, Carousel, BottomNav, AccountMenu, StoreCard, bookById, BOOKS, STORES, CAROUSEL } = window;

const COLORS = [
  ['Primary',        '#553522', 'espresso'],
  ['Secondary',      '#8D5B3D', 'coffee'],
  ['Accent',         '#C08552', 'caramel'],
  ['Active / Burnt',  '#7C3900', 'burnt'],
  ['Background',     '#FFE9CF', 'bg-peach'],
  ['Background Alt', '#FFF8F0', 'bg-cream'],
  ['Surface / Card', '#FFFFFF', 'surface'],
  ['Text Secondary', '#999999', 'ink-secondary'],
  ['Success',        '#6F8E5A', 'success'],
  ['Error',          '#B76464', 'error'],
];

const TYPE = [
  ['H1 — Heading',      't-h1',     'Hello !',                 'Montserrat ExtraBold · 45'],
  ['H2 — Heading',      't-h2',     'Our Library',             'Montserrat Bold · 26'],
  ['H3 — Subheading',   't-h3',     'Featured Books',          'Montserrat Bold · 20'],
  ['Body Large',        't-body-lg','A timeless manual on discipline and perspective.', 'Montserrat Medium · 15'],
  ['Body Medium',       't-body',   'Warm, practical letters on friendship and mortality.', 'Montserrat Medium · 13.5'],
  ['Caption',           't-caption','STOIC PHILOSOPHY',        'Montserrat SemiBold · 11.5'],
];

function Section({ id, label, title, children }){
  return (
    <section style={{ marginBottom:64 }}>
      <div style={{ display:'flex', alignItems:'baseline', gap:14, marginBottom:22 }}>
        <span style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:12, letterSpacing:2,
          color:'var(--caramel)' }}>{label}</span>
        <span style={{ flex:1, height:1, background:'rgba(85,53,34,.14)' }}/>
      </div>
      <h2 className="t-h2" style={{ fontSize:24, margin:'0 0 22px' }}>{title}</h2>
      {children}
    </section>
  );
}
function Card({ children, pad=24, style }){
  return <div style={{ background:'var(--surface)', borderRadius:18, padding:pad,
    boxShadow:'0 8px 24px rgba(85,53,34,.1)', ...style }}>{children}</div>;
}

function App(){
  const [focusDemo] = useState('Jessie Lawrence');
  return (
    <div style={{ maxWidth:1000, margin:'0 auto', padding:'56px 28px 90px' }}>
      {/* header */}
      <header style={{ display:'flex', alignItems:'center', gap:20, marginBottom:14 }}>
        <Logo size={70} />
        <div>
          <div className="t-h2" style={{ fontSize:30 }}>BOOWORM</div>
          <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:14, color:'var(--espresso-2)', opacity:.8 }}>
            Design System · colours, type & components</div>
        </div>
        <a href="D'Classics Books.html" style={{ marginLeft:'auto', textDecoration:'none' }}>
          <span className="dc-btn" style={{ display:'inline-flex', width:'auto', padding:'0 22px', height:44 }}>
            Open Prototype →</span>
        </a>
      </header>
      <p className="t-body-lg" style={{ maxWidth:560, marginBottom:48, color:'var(--espresso-2)' }}>
        A warm, literary system for an elegant mobile bookstore. Every screen in the prototype is built from
        the tokens and components below.
      </p>

      {/* COLORS */}
      <Section label="01" title="Colour Styles">
        <div style={{ display:'grid', gridTemplateColumns:'repeat(auto-fill,minmax(160px,1fr))', gap:16 }}>
          {COLORS.map(([name,hex,v])=>(
            <Card key={name} pad={0} style={{ overflow:'hidden' }}>
              <div style={{ height:84, background:hex, borderBottom:'1px solid rgba(0,0,0,.05)' }}/>
              <div style={{ padding:'12px 14px' }}>
                <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:13, color:'var(--espresso)' }}>{name}</div>
                <div style={{ fontFamily:'var(--font-input)', fontWeight:500, fontSize:11.5, color:'var(--ink-secondary)', marginTop:3 }}>{hex}</div>
              </div>
            </Card>
          ))}
        </div>
      </Section>

      {/* TYPE */}
      <Section label="02" title="Typography">
        <Card>
          {TYPE.map(([name,cls,sample,meta],i)=>(
            <div key={name} style={{ display:'flex', alignItems:'baseline', gap:24, padding:'16px 0',
              borderTop: i? '1px solid rgba(85,53,34,.08)':'none' }}>
              <div style={{ width:140, flexShrink:0 }}>
                <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:12.5, color:'var(--espresso)' }}>{name}</div>
                <div style={{ fontFamily:'var(--font-input)', fontWeight:500, fontSize:11, color:'var(--ink-secondary)', marginTop:2 }}>{meta}</div>
              </div>
              <div className={cls} style={{ flex:1 }}>{sample}</div>
            </div>
          ))}
          <div style={{ display:'flex', gap:28, marginTop:18, paddingTop:18, borderTop:'1px solid rgba(85,53,34,.08)' }}>
            <div>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:11, letterSpacing:1.5, color:'var(--caramel)', marginBottom:8 }}>SANS · MONTSERRAT</div>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:26, color:'var(--espresso)' }}>Aa Bb Cc 123</div>
            </div>
            <div>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:11, letterSpacing:1.5, color:'var(--caramel)', marginBottom:8 }}>SERIF · PLAYFAIR</div>
              <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:26, color:'var(--espresso)' }}>Aa Bb Cc 123</div>
            </div>
          </div>
        </Card>
      </Section>

      {/* COMPONENTS */}
      <Section label="03" title="Buttons">
        <Card>
          <div style={{ display:'flex', flexWrap:'wrap', gap:20, alignItems:'flex-end' }}>
            {[['Default','#8D5B3D'],['Hover','#7A4D32'],['Pressed','#623C26']].map(([lbl,bg])=>(
              <div key={lbl} style={{ width:170 }}>
                <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:11, color:'var(--ink-secondary)', marginBottom:8 }}>{lbl}</div>
                <div className="dc-btn" style={{ background:bg, cursor:'default', transform: lbl==='Pressed'?'translateY(1px)':'none' }}>LOGIN</div>
              </div>
            ))}
            <div style={{ width:170 }}>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:11, color:'var(--ink-secondary)', marginBottom:8 }}>Disabled</div>
              <button className="dc-btn" disabled>LOGIN</button>
            </div>
          </div>
        </Card>
      </Section>

      <Section label="04" title="Text Fields">
        <Card>
          <div style={{ display:'grid', gridTemplateColumns:'1fr 1fr', gap:'4px 28px' }}>
            <div><div className="field-label">Default</div><input className="dc-input" placeholder="Enter your username" readOnly/><div className="field-error"/></div>
            <div><div className="field-label">Focused</div><input className="dc-input" defaultValue="jessiela" style={{ outline:'2px solid var(--caramel)', boxShadow:'0 6px 12px rgba(141,91,61,.22)' }} readOnly/><div className="field-error"/></div>
            <div><div className="field-label">Filled</div><input className="dc-input is-filled" defaultValue="Jessie Lawrence" readOnly/><div className="field-error"/></div>
            <div><div className="field-label">Error</div><input className="dc-input is-error" defaultValue="123" readOnly/><div className="field-error"><span style={{fontWeight:800}}>!</span>Password must be at least 6 characters</div></div>
          </div>
        </Card>
      </Section>

      <Section label="05" title="Book Card — Featured · Standard · Compact">
        <Card>
          <div style={{ display:'flex', gap:44, alignItems:'flex-start', flexWrap:'wrap' }}>
            <div><div style={DSlbl}>Featured</div><BookCard book={bookById('gatsby')} w={150} /></div>
            <div><div style={DSlbl}>Standard</div><BookCard book={bookById('pride')} w={124} showTag /></div>
            <div><div style={DSlbl}>Compact</div><BookCard book={bookById('republic')} w={92} /></div>
          </div>
        </Card>
      </Section>

      <Section label="06" title="Store Card & Carousel">
        <div style={{ display:'grid', gridTemplateColumns:'1fr 1fr', gap:24, alignItems:'start' }}>
          <Card style={{ background:'var(--bg-peach)' }}><StoreCard store={STORES[0]} /></Card>
          <Card style={{ background:'var(--bg-peach)' }}><Carousel slides={CAROUSEL} /></Card>
        </div>
      </Section>

      <Section label="07" title="Navigation">
        <Card style={{ background:'var(--bg-peach)', position:'relative', height:128, overflow:'hidden' }}>
          <div style={{ position:'absolute', top:18, right:24 }}>
            <AccountMenu items={[{id:'home',icon:'home',label:'Home'},{id:'books',icon:'book',label:'Books'},{id:'logout',icon:'logout',label:'Log Out'}]} onPick={()=>{}} />
          </div>
          <div style={{ position:'absolute', left:0, right:0, bottom:0, height:96 }}>
            <BottomNav active="home" onNav={()=>{}} />
          </div>
        </Card>
        <div style={{ display:'grid', gridTemplateColumns:'repeat(4,1fr)', gap:16, marginTop:16 }}>
          {[['home','Home'],['books','Books'],['stores','Stores'],['logout','Log Out']].map(([id,lbl])=>(
            <Card key={id} style={{ background:'var(--bg-peach)', position:'relative', height:96, overflow:'hidden' }}>
              <div style={{ position:'absolute', left:0, right:0, bottom:-8, transform:'scale(.62)', transformOrigin:'bottom center' }}>
                <BottomNav active={id} onNav={()=>{}} />
              </div>
              <div style={{ position:'absolute', top:10, left:0, right:0, textAlign:'center', fontFamily:'var(--font-sans)', fontWeight:700, fontSize:10, letterSpacing:.5, color:'var(--ink-secondary)' }}>{lbl} active</div>
            </Card>
          ))}
        </div>
      </Section>

      <footer style={{ textAlign:'center', fontFamily:'var(--font-sans)', fontWeight:600, fontSize:12, color:'var(--ink-secondary)', marginTop:30 }}>
        BOOWORM — built from the UX-LAB Figma design system
      </footer>
    </div>
  );
}
const DSlbl = { fontFamily:'var(--font-sans)', fontWeight:600, fontSize:11, color:'var(--ink-secondary)', marginBottom:12 };

ReactDOM.createRoot(document.getElementById('root')).render(<App />);
})();
