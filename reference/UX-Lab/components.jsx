// components.jsx — BOOWORM shared component library
// Reads window.BOOKS / STORES at render time. Exports to window at end.
(function(){
const { useState, useEffect, useRef } = React;

/* ───────────────────────── Icons (currentColor) ───────────────────────── */
const I = {
  home:   <path d="M4 11.5 12 4l8 7.5M6 10v9h12v-9M10 19v-5h4v5" fill="none" stroke="currentColor" strokeWidth="1.9" strokeLinecap="round" strokeLinejoin="round"/>,
  book:   <path d="M12 6.5C10.5 5.2 8 4.6 4.5 4.8v12c3.4-.2 6 .4 7.5 1.6 1.5-1.2 4.1-1.8 7.5-1.6v-12C16 4.6 13.5 5.2 12 6.5Zm0 0v12" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>,
  store:  <path d="M5 9.5V19h14V9.5M3.5 9.5 5 4.5h14l1.5 5a2.4 2.4 0 0 1-4.5 1 2.4 2.4 0 0 1-4.5 0 2.4 2.4 0 0 1-4.5 0 2.4 2.4 0 0 1-4.5-1ZM10 19v-4.5h4V19" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round"/>,
  logout: <path d="M14 7V5.5A1.5 1.5 0 0 0 12.5 4h-7A1.5 1.5 0 0 0 4 5.5v13A1.5 1.5 0 0 0 5.5 20h7a1.5 1.5 0 0 0 1.5-1.5V17M9 12h11m0 0-3.5-3.5M20 12l-3.5 3.5" fill="none" stroke="currentColor" strokeWidth="1.9" strokeLinecap="round" strokeLinejoin="round"/>,
  back:   <path d="M15 5l-7 7 7 7" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round"/>,
  chevL:  <path d="M9 5L4 9.5 9 14" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>,
  chevR:  <path d="M5 5l5 4.5L5 14" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>,
  pin:    <path d="M10 18s6-5 6-9.5A6 6 0 0 0 4 8.5C4 13 10 18 10 18Z M10 6.4a2.2 2.2 0 1 1 0 4.4 2.2 2.2 0 0 1 0-4.4Z" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/>,
  phone:  <path d="M5 4h3l1.5 4-2 1.2a10 10 0 0 0 4.3 4.3L13 15.5 17 17v3a1 1 0 0 1-1 1A13 13 0 0 1 4 5a1 1 0 0 1 1-1Z" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"/>,
  clock:  <g fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round"><circle cx="10" cy="10" r="7"/><path d="M10 6v4l2.6 1.6"/></g>,
  check:  <path d="M5 13l4.2 4.2L19 7" fill="none" stroke="currentColor" strokeWidth="2.6" strokeLinecap="round" strokeLinejoin="round"/>,
  google: <g><path d="M19.6 10.2c0-.7-.06-1.2-.18-1.8H10v3.4h5.5c-.1.9-.7 2.3-2 3.2l-.02.12 2.9 2.24.2.02c1.84-1.7 2.9-4.2 2.9-7.18Z" fill="#4285F4"/><path d="M10 20c2.6 0 4.8-.86 6.4-2.34l-3.05-2.36c-.82.57-1.9.97-3.35.97a5.8 5.8 0 0 1-5.5-4l-.11.01-3 2.32-.04.11A10 10 0 0 0 10 20Z" fill="#34A853"/><path d="M4.5 11.9a6.16 6.16 0 0 1-.33-1.9c0-.66.12-1.3.32-1.9l-.005-.13-3.04-2.36-.1.05A10 10 0 0 0 0 10c0 1.6.39 3.13 1.06 4.48L4.5 11.9Z" fill="#FBBC05"/><path d="M10 3.96c1.84 0 3.08.8 3.79 1.46l2.77-2.7C14.79.99 12.6 0 10 0A10 10 0 0 0 1.06 5.52L4.5 8.1A5.8 5.8 0 0 1 10 3.96Z" fill="#EA4335"/></g>,
};
function Icon({ d, size = 22, color = 'currentColor', stroke }){
  return <svg width={size} height={size} viewBox="0 0 24 24" style={{ color, display:'block' }}>{d}</svg>;
}

/* ───────────────────────── Logo ───────────────────────── */
function Logo({ size = 92 }){
  return <img src="assets/logo.png" alt="BOOWORM" style={{ width:size, height:'auto', display:'block' }} draggable="false" />;
}

/* ───────────────────────── Clothbound cover (placeholder) ───────────────────────── */
function BookCover({ book, w = 120 }){
  const h = Math.round(w * 1.5);
  const pad = Math.round(w * 0.085);
  return (
    <div style={{
      width:w, height:h, position:'relative', borderRadius:'2px 5px 5px 2px', overflow:'hidden',
      background:book.cloth,
      boxShadow:`inset ${Math.round(w*0.05)}px 0 ${Math.round(w*0.09)}px rgba(0,0,0,0.28), inset 0 0 0 1px rgba(255,255,255,0.04)`,
    }}>
      {/* cloth texture */}
      <div style={{ position:'absolute', inset:0, opacity:0.14, mixBlendMode:'overlay',
        backgroundImage:'repeating-linear-gradient(90deg,#fff 0 1px,transparent 1px 3px),repeating-linear-gradient(0deg,#fff 0 1px,transparent 1px 3px)' }}/>
      {/* spine shading */}
      <div style={{ position:'absolute', left:0, top:0, bottom:0, width:Math.max(4,w*0.05),
        background:'linear-gradient(90deg,rgba(255,255,255,.18),rgba(0,0,0,.22))' }}/>
      {/* gold frame */}
      <div style={{ position:'absolute', inset:pad, border:`1px solid ${book.accent}`, borderRadius:2, opacity:.85 }}/>
      <div style={{ position:'absolute', inset:pad+3, border:`0.5px solid ${book.accent}`, borderRadius:1, opacity:.5 }}/>
      {/* title block */}
      <div style={{ position:'absolute', inset:pad+Math.round(w*0.06), display:'flex', flexDirection:'column',
        alignItems:'center', justifyContent:'center', textAlign:'center', gap:Math.round(w*0.05) }}>
        <div style={{ width:'40%', height:1, background:book.accent, opacity:.7 }}/>
        <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, color:book.accent,
          fontSize:Math.max(10.5, w*0.105), lineHeight:1.1, letterSpacing:.2 }}>{book.title}</div>
        <div style={{ width:'40%', height:1, background:book.accent, opacity:.7 }}/>
        <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, color:book.accent, opacity:.92,
          fontSize:Math.max(7, w*0.062), letterSpacing:1.4, textTransform:'uppercase', marginTop:Math.round(w*0.02) }}>{book.author}</div>
      </div>
    </div>
  );
}

/* ───────────────────────── Book card (Home / Books) ───────────────────────── */
function BookCard({ book, w = 124, showTag = false, onClick }){
  const [hover, setHover] = useState(false);
  const [press, setPress] = useState(false);
  const coverW = w;
  return (
    <button onClick={onClick}
      onMouseEnter={()=>setHover(true)} onMouseLeave={()=>{setHover(false);setPress(false);}}
      onMouseDown={()=>setPress(true)} onMouseUp={()=>setPress(false)}
      style={{ background:'none', border:'none', padding:0, cursor:'pointer', textAlign:'left',
        width:coverW+8, font: 'inherit',
        transform: press ? 'scale(.97)' : hover ? 'translateY(-4px)' : 'none',
        transition:'transform .18s cubic-bezier(.2,.7,.3,1)' }}>
      <div style={{ position:'relative', width:coverW+8, height:Math.round(coverW*1.5)+8 }}>
        {/* page block behind */}
        <div style={{ position:'absolute', right:0, top:6, width:coverW, height:Math.round(coverW*1.5),
          background:'#F4ECE0', borderRadius:'2px 5px 5px 2px',
          boxShadow:'inset -3px 0 0 #E7DAC8, inset -6px 0 0 #EFE4D4' }}/>
        {/* ribbon */}
        <div style={{ position:'absolute', left:coverW*0.78, top:-2, width:10, height:Math.round(coverW*1.5)+18,
          background:'var(--caramel)', clipPath:'polygon(0 0,100% 0,100% 100%,50% 86%,0 100%)',
          boxShadow:'0 2px 4px rgba(0,0,0,.18)', zIndex:3 }}/>
        {/* cover */}
        <div style={{ position:'absolute', left:0, top:0, zIndex:2,
          boxShadow: hover ? 'var(--shadow-float)' : 'var(--shadow-card)', borderRadius:'2px 5px 5px 2px',
          transition:'box-shadow .18s ease' }}>
          <BookCover book={book} w={coverW} />
        </div>
      </div>
      <div style={{ marginTop:12, paddingRight:4 }}>
        {showTag && <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:9, letterSpacing:1,
          textTransform:'uppercase', color:'var(--ink-secondary)', marginBottom:4 }}>{book.tag}</div>}
        <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:12.5, color:'var(--caramel)',
          lineHeight:1.2 }}>{book.title}</div>
        <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:13.5, color:'var(--coffee)',
          marginTop:3 }}>{book.author}</div>
      </div>
    </button>
  );
}

/* ───────────────────────── Text field ───────────────────────── */
function TextField({ label, value, onChange, placeholder, error, type='text', onEnter }){
  return (
    <div style={{ marginBottom:16 }}>
      {label && <label className="field-label">{label}</label>}
      <input
        className={'dc-input' + (error ? ' is-error' : '') + (value ? ' is-filled' : '')}
        type={type} value={value} placeholder={placeholder}
        onChange={e=>onChange(e.target.value)}
        onKeyDown={e=>{ if(e.key==='Enter' && onEnter) onEnter(); }} />
      {error && <div className="field-error"><span style={{fontWeight:800}}>!</span>{error}</div>}
    </div>
  );
}

/* ───────────────────────── Store carousel ───────────────────────── */
function Carousel({ slides, onOpen }){
  const [i, setI] = useState(0);
  const [paused, setPaused] = useState(false);
  const n = slides.length;
  const go = (d)=> setI(p => (p + d + n) % n);
  useEffect(()=>{
    if(paused) return;
    const t = setInterval(()=> setI(p => (p+1)%n), 4200);
    return ()=> clearInterval(t);
  }, [paused, n]);
  return (
    <div onMouseEnter={()=>setPaused(true)} onMouseLeave={()=>setPaused(false)}>
      <div style={{ position:'relative', height:150, borderRadius:16, overflow:'hidden',
        boxShadow:'var(--shadow-card)' }}>
        <div style={{ display:'flex', height:'100%', transform:`translateX(-${i*100}%)`,
          transition:'transform .55s cubic-bezier(.5,.05,.2,1)' }}>
          {slides.map((s,k)=>(
            <button key={k} onClick={()=>onOpen && onOpen()} style={{ flex:'0 0 100%', position:'relative',
              border:'none', padding:0, cursor:'pointer', background:`#3a2a1c url(${s.img}) center/cover no-repeat` }}>
              <div style={{ position:'absolute', inset:0, background:'linear-gradient(180deg,rgba(20,12,6,0) 45%,rgba(20,12,6,.55))' }}/>
              <div style={{ position:'absolute', left:14, bottom:13 }}>
                <div style={{ display:'inline-flex', alignItems:'center', gap:5, background:'rgba(28,18,10,.55)',
                  backdropFilter:'blur(4px)', borderRadius:'var(--r-pill)', padding:'5px 11px',
                  fontFamily:'var(--font-sans)', fontWeight:700, fontSize:10.5, letterSpacing:.5,
                  textTransform:'uppercase', color:'#fff' }}>
                  <svg width="12" height="12" viewBox="0 0 20 20" style={{color:'#fff'}}>{I.pin}</svg>{s.city}</div>
              </div>
            </button>
          ))}
        </div>
      </div>
      <div style={{ display:'flex', alignItems:'center', justifyContent:'center', gap:14, marginTop:11 }}>
        <CarBtn onClick={()=>go(-1)} dir="chevL" />
        <div style={{ display:'flex', gap:7 }}>
          {slides.map((_,k)=>(
            <button key={k} onClick={()=>setI(k)} aria-label={'slide '+(k+1)} style={{ border:'none', cursor:'pointer', padding:0,
              width:k===i?20:8, height:8, borderRadius:8, background:k===i?'var(--burnt)':'rgba(124,57,0,.28)',
              transition:'all .3s ease' }}/>
          ))}
        </div>
        <CarBtn onClick={()=>go(1)} dir="chevR" />
      </div>
    </div>
  );
}
function CarBtn({ onClick, dir }){
  const [h,setH] = useState(false);
  return (
    <button onClick={onClick} onMouseEnter={()=>setH(true)} onMouseLeave={()=>setH(false)}
      style={{ width:30, height:30, borderRadius:'50%', border:'none', cursor:'pointer',
        background:h?'var(--coffee)':'var(--surface)', color:h?'#fff':'var(--coffee)',
        boxShadow:'0 3px 8px rgba(85,53,34,.2)', display:'flex', alignItems:'center', justifyContent:'center',
        transition:'all .16s ease' }}>
      <svg width="18" height="18" viewBox="0 0 18 18">{I[dir]}</svg>
    </button>
  );
}

/* ───────────────────────── Floating bottom nav (pill) ───────────────────────── */
const NAV_ITEMS = [
  { id:'home',   label:'Home',   icon:'home'   },
  { id:'books',  label:'Books',  icon:'book'   },
  { id:'stores', label:'Stores', icon:'store'  },
  { id:'logout', label:'Log Out',icon:'logout' },
];
function BottomNav({ active, onNav }){
  const items = NAV_ITEMS;
  const W = 312, PAD = 36, slot = (W - PAD*2) / items.length; // 60
  const T = 12, barH = 52, H = T + barH;                       // bar top / total height
  const k = 1.4, R = 20;                                       // scoop scale / corner radius
  const center = (i)=> PAD + slot*(i+0.5);
  const idx = Math.max(0, items.findIndex(it=>it.id===active));
  const target = center(idx);
  const [cx, setCx] = useState(target);

  useEffect(()=>{
    let raf; const from = cx, to = target, t0 = performance.now(), dur = 360;
    const tick = (now)=>{ const t = Math.min(1,(now-t0)/dur); const e = 1-Math.pow(1-t,3);
      setCx(from+(to-from)*e); if(t<1) raf = requestAnimationFrame(tick); };
    raf = requestAnimationFrame(tick); return ()=> cancelAnimationFrame(raf);
  }, [target]);

  // scoop path mapped from the Figma "Subtract" curve, scaled by k around cx
  const sx = (x)=> cx - 18.974*k + x*k;
  const sy = (y)=> T + y*k;
  const cyB = T + 6.97*k;                 // bubble centre (Figma: 6.97 below bar top)
  const rB = 11.77*k;                      // bubble radius
  const path = [
    `M ${R} ${T}`,
    `H ${sx(0).toFixed(2)}`,
    `C ${sx(5.764).toFixed(2)} ${sy(2.642).toFixed(2)}, ${sx(0.24).toFixed(2)} ${sy(21.856).toFixed(2)}, ${sx(18.734).toFixed(2)} ${sy(21.856).toFixed(2)}`,
    `C ${sx(37.229).toFixed(2)} ${sy(21.856).toFixed(2)}, ${sx(30.984).toFixed(2)} ${sy(0).toFixed(2)}, ${sx(37.949).toFixed(2)} ${sy(0).toFixed(2)}`,
    `H ${W-R}`,
    `A ${R} ${R} 0 0 1 ${W} ${T+R}`,
    `V ${H-R}`,
    `A ${R} ${R} 0 0 1 ${W-R} ${H}`,
    `H ${R}`,
    `A ${R} ${R} 0 0 1 0 ${H-R}`,
    `V ${T+R}`,
    `A ${R} ${R} 0 0 1 ${R} ${T}`,
    'Z',
  ].join(' ');

  return (
    <div style={{ position:'absolute', left:'50%', bottom:24, transform:'translateX(-50%)', zIndex:40,
      width:W, height:H }}>
      <svg width={W} height={H} viewBox={`0 0 ${W} ${H}`} style={{ position:'absolute', inset:0,
        filter:'drop-shadow(0 10px 22px rgba(85,53,34,.34))', overflow:'visible' }}>
        <path d={path} fill="var(--caramel)" />
      </svg>
      {/* raised active bubble */}
      <div style={{ position:'absolute', left:cx, top:cyB, width:rB*2, height:rB*2, marginLeft:-rB, marginTop:-rB,
        borderRadius:'50%', background:'var(--burnt)', boxShadow:'0 7px 14px rgba(124,57,0,.45)',
        display:'flex', alignItems:'center', justifyContent:'center', color:'var(--bg-cream)', pointerEvents:'none' }}>
        <svg width="20" height="20" viewBox="0 0 24 24">{I[items[idx].icon]}</svg>
      </div>
      {/* tap targets + inactive icons */}
      {items.map((it,i)=>{
        const on = i===idx;
        return (
          <button key={it.id} onClick={()=>onNav(it.id)} title={it.label} aria-label={it.label}
            style={{ position:'absolute', top:0, left:center(i)-slot/2, width:slot, height:H, border:'none',
              background:'none', cursor:'pointer', display:'flex', alignItems:'center', justifyContent:'center' }}>
            <svg width="22" height="22" viewBox="0 0 24 24" style={{ marginTop: T,
              color: on ? 'transparent' : 'var(--espresso)', opacity: on ? 0 : 1,
              transition:'color .2s ease, opacity .2s ease' }}>{I[it.icon]}</svg>
          </button>
        );
      })}
    </div>
  );
}

/* ───────────────────────── Account button + dropdown menu ───────────────────────── */
function AccountMenu({ items, onPick }){
  const [open, setOpen] = useState(false);
  const ref = useRef(null);
  useEffect(()=>{
    const h = (e)=>{ if(ref.current && !ref.current.contains(e.target)) setOpen(false); };
    document.addEventListener('mousedown', h);
    return ()=> document.removeEventListener('mousedown', h);
  },[]);
  return (
    <div ref={ref} style={{ position:'relative', zIndex:30 }}>
      <button onClick={()=>setOpen(o=>!o)} aria-label="Menu"
        style={{ width:46, height:46, borderRadius:'50%', border:'none', cursor:'pointer',
          background:'var(--surface)', boxShadow:'0 4px 12px rgba(85,53,34,.18)',
          display:'flex', alignItems:'center', justifyContent:'center', color:'var(--espresso)',
          transition:'transform .15s ease', transform: open?'scale(.94)':'none' }}>
        <svg width="30" height="30" viewBox="0 0 24 24" style={{color:'var(--espresso)'}}>
          <circle cx="12" cy="12" r="10.4" fill="none" stroke="currentColor" strokeWidth="1.7"/>
          <circle cx="12" cy="9.6" r="3.1" fill="none" stroke="currentColor" strokeWidth="1.7"/>
          <path d="M6.2 18.2a6 6 0 0 1 11.6 0" fill="none" stroke="currentColor" strokeWidth="1.7"/>
        </svg>
      </button>
      {open && (
        <div style={{ position:'absolute', right:0, top:54, minWidth:170,
          background:'var(--surface)', borderRadius:14, padding:7,
          boxShadow:'0 18px 40px rgba(85,53,34,.28)', animation:'dc-pop .16s ease both',
          transformOrigin:'top right' }}>
          {items.map((it,k)=>(
            <button key={it.id} onClick={()=>{ setOpen(false); onPick(it.id); }}
              onMouseEnter={e=>e.currentTarget.style.background = it.id==='logout' ? 'rgba(183,100,100,.1)' : 'var(--bg-peach)'}
              onMouseLeave={e=>e.currentTarget.style.background='transparent'}
              style={{ display:'flex', alignItems:'center', gap:11, width:'100%', textAlign:'left',
                padding:'11px 13px', border:'none', background:'transparent', cursor:'pointer',
                borderRadius:9, color: it.id==='logout'?'var(--error)':'var(--espresso)',
                fontFamily:'var(--font-sans)', fontWeight:700, fontSize:13.5,
                borderTop: k>0 && it.id==='logout' ? '1px solid rgba(85,53,34,.08)':'none',
                transition:'background .14s' }}>
              <svg width="19" height="19" viewBox="0 0 24 24" style={{color:'inherit'}}>{I[it.icon]}</svg>
              {it.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

Object.assign(window, { I, Icon, Logo, BookCover, BookCard, TextField, Carousel, CarBtn, BottomNav, AccountMenu, NAV_ITEMS });
})();
