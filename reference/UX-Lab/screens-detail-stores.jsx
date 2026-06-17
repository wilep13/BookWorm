// screens-detail-stores.jsx — Book Detail (order form) + Stores
(function(){
const { useState } = React;
const { BookCover, TextField, I, Header, BottomNav, bookById, STORES, rupiah } = window;

const qtyBtn = (disabled)=>({
  width:32, height:32, borderRadius:'50%', border:'1.5px solid rgba(141,91,61,.4)',
  background: disabled ? 'transparent' : 'var(--bg-cream)', cursor: disabled?'default':'pointer',
  color: disabled ? 'rgba(141,91,61,.4)' : 'var(--coffee)', fontFamily:'var(--font-sans)', fontWeight:700,
  fontSize:18, lineHeight:1, display:'flex', alignItems:'center', justifyContent:'center', paddingBottom:2,
});

const Fleuron = ({ size=14, color='var(--caramel)', rot=0 })=>(
  <span aria-hidden="true" style={{ fontFamily:'var(--font-serif)', fontSize:size, color, lineHeight:1,
    display:'inline-block', transform:`rotate(${rot}deg)` }}>❦</span>
);
function OrnDivider({ width=120 }){
  return (
    <div style={{ display:'flex', alignItems:'center', justifyContent:'center', gap:12 }}>
      <span style={{ width:width/2, height:1, background:'linear-gradient(to left, var(--caramel), rgba(192,133,82,0))' }}/>
      <span aria-hidden="true" style={{ fontFamily:'var(--font-serif)', fontSize:16, color:'var(--caramel)', lineHeight:1 }}>❧</span>
      <span style={{ width:width/2, height:1, background:'linear-gradient(to right, var(--caramel), rgba(192,133,82,0))' }}/>
    </div>
  );
}

function DetailScreen({ bookId, onNav, onBack }){
  const book = bookById(bookId);
  const [addr, setAddr] = useState('');
  const [phone, setPhone] = useState('');
  const [qty, setQty] = useState(1);
  const [err, setErr] = useState({});
  const [done, setDone] = useState(false);

  const buy = ()=> setDone(true);
  const metaItem = (label, value)=>(
    <div style={{ flex:1, textAlign:'center' }}>
      <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:16, color:'var(--espresso)' }}>{value}</div>
      <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:9, letterSpacing:1,
        textTransform:'uppercase', color:'var(--ink-secondary)', marginTop:3 }}>{label}</div>
    </div>
  );

  return (
    <div style={{ position:'relative', height:'100%', background:'var(--bg-peach)', overflow:'hidden', display:'flex', flexDirection:'column' }}>
      <div aria-hidden="true" style={{ position:'absolute', inset:0, pointerEvents:'none', opacity:.7,
        backgroundImage:'repeating-linear-gradient(0deg, rgba(141,91,61,.035) 0 1px, transparent 1px 5px)' }}/>
      <div className="noscroll" style={{ flex:1, minHeight:0, overflowY:'auto', paddingBottom:40, position:'relative' }}>
        {/* top bar */}
        <div style={{ display:'flex', alignItems:'center', gap:14, padding:'64px 24px 0' }}>
          <button onClick={onBack} aria-label="Back" style={{ width:44, height:44, borderRadius:'50%', border:'none',
            cursor:'pointer', background:'var(--surface)', color:'var(--espresso)', boxShadow:'0 4px 12px rgba(85,53,34,.16)',
            display:'flex', alignItems:'center', justifyContent:'center' }}>
            <svg width="22" height="22" viewBox="0 0 24 24">{I.back}</svg>
          </button>
          <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:15, color:'var(--espresso)' }}>Book Detail</div>
        </div>

        {/* hero cover — staged & framed */}
        <div style={{ position:'relative', display:'flex', justifyContent:'center', padding:'20px 24px 0' }}>
          <div aria-hidden="true" style={{ position:'absolute', top:-8, left:'50%', transform:'translateX(-50%)',
            fontFamily:'var(--font-serif)', fontSize:188, lineHeight:1, color:'var(--coffee)', opacity:.05, pointerEvents:'none' }}>❦</div>
          <div style={{ position:'absolute', top:36, left:'50%', transform:'translateX(-50%)', width:230, height:160,
            background:'radial-gradient(closest-side, rgba(192,133,82,.5), rgba(192,133,82,0))', filter:'blur(6px)' }}/>
          <div style={{ position:'relative', filter:'drop-shadow(0 20px 26px rgba(85,53,34,.4))' }}>
            <BookCover book={book} w={162} />
            {[['top','left'],['top','right'],['bottom','left'],['bottom','right']].map(([v,h])=>(
              <span key={v+h} aria-hidden="true" style={{ position:'absolute', [v]:-11, [h]:-11, width:15, height:15,
                ['border'+(v==='top'?'Top':'Bottom')]:'2px solid var(--caramel)',
                ['border'+(h==='left'?'Left':'Right')]:'2px solid var(--caramel)' }}/>
            ))}
            <div style={{ position:'absolute', left:'8%', right:'8%', bottom:-12, height:16,
              background:'radial-gradient(closest-side, rgba(85,53,34,.34), rgba(85,53,34,0))' }}/>
          </div>
        </div>

        {/* title block */}
        <div style={{ padding:'30px 28px 0', textAlign:'center' }}>
          <div style={{ display:'inline-block', fontFamily:'var(--font-sans)', fontWeight:700, fontSize:10,
            letterSpacing:1.4, textTransform:'uppercase', color:'var(--caramel)',
            border:'1px solid rgba(192,133,82,.5)', borderRadius:'var(--r-pill)', padding:'5px 12px', marginBottom:14 }}>
            {book.tag}</div>
          <div className="t-h2" style={{ fontSize:25 }}>{book.title}</div>
          <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:16, color:'var(--coffee)', marginTop:6 }}>
            by {book.author}</div>
        </div>

        {/* ornamental divider */}
        <div style={{ padding:'18px 24px 0' }}><OrnDivider /></div>

        {/* meta strip */}
        <div style={{ display:'flex', margin:'18px 24px 0', background:'rgba(255,255,255,.5)', borderRadius:14,
          padding:'13px 6px' }}>
          {metaItem('Published', book.year)}
          <div style={{ width:1, background:'rgba(85,53,34,.12)' }}/>
          {metaItem('Pages', book.pages)}
          <div style={{ width:1, background:'rgba(85,53,34,.12)' }}/>
          {metaItem('Genre', book.cat)}
        </div>

        {/* description */}
        <div style={{ padding:'26px 28px 0' }}>
          <div style={{ display:'flex', alignItems:'center', justifyContent:'center', gap:10, marginBottom:14 }}>
            <Fleuron size={12} />
            <span style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:12, letterSpacing:1.4,
              textTransform:'uppercase', color:'var(--espresso)' }}>About this book</span>
            <Fleuron size={12} rot={180} />
          </div>
          <p className="t-body" style={{ margin:0, color:'var(--espresso-2)', textWrap:'pretty', textAlign:'justify' }}>
            <span style={{ float:'left', fontFamily:'var(--font-serif)', fontWeight:700, fontSize:46, lineHeight:.74,
              color:'var(--coffee)', margin:'5px 10px 0 0' }}>{book.blurb.charAt(0)}</span>
            {book.blurb.slice(1)}
          </p>
        </div>

        {/* order form */}
        <div style={{ margin:'24px 24px 0', background:'var(--surface)', borderRadius:18, padding:'22px 20px',
          boxShadow:'var(--shadow-card)' }}>
          <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:15, color:'var(--espresso)', marginBottom:16 }}>Place your order</div>
          {/* quantity row */}
          <div style={{ display:'flex', alignItems:'center', justifyContent:'space-between', marginBottom:18 }}>
            <div>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:12.5, color:'var(--espresso)' }}>Quantity</div>
              <div style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:11, color:'var(--ink-secondary)', marginTop:2 }}>{rupiah(book.price)} each</div>
            </div>
            <div style={{ display:'flex', alignItems:'center', gap:14 }}>
              <button onClick={()=>setQty(q=>Math.max(1,q-1))} aria-label="minus" style={qtyBtn(qty<=1)}>−</button>
              <span style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:17, color:'var(--espresso)', minWidth:18, textAlign:'center' }}>{qty}</span>
              <button onClick={()=>setQty(q=>Math.min(9,q+1))} aria-label="plus" style={qtyBtn(false)}>+</button>
            </div>
          </div>
          <TextField label="Delivery Address" value={addr} onChange={v=>{setAddr(v); setErr({...err,addr:null});}}
            placeholder="Enter delivery address" error={err.addr} />
          <TextField label="Phone Number" type="tel" value={phone} onChange={v=>{setPhone(v); setErr({...err,phone:null});}}
            placeholder="Enter phone number" error={err.phone} onEnter={buy} />
          <div style={{ display:'flex', alignItems:'center', justifyContent:'space-between', margin:'4px 2px 16px' }}>
            <span style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:13, color:'var(--espresso)' }}>Total</span>
            <span style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:21, color:'var(--coffee)' }}>{rupiah(book.price*qty)}</span>
          </div>
          <button className="dc-btn" onClick={buy}>BUY NOW</button>
        </div>
      </div>

      {/* success dialog (Material style) */}
      {done && (
        <div style={{ position:'absolute', inset:0, zIndex:60, display:'flex', alignItems:'center', justifyContent:'center',
          background:'rgba(40,24,12,.5)' }}>
          <div style={{ width:280, background:'var(--surface)', borderRadius:22, padding:'30px 26px 22px',
            textAlign:'center', boxShadow:'0 30px 60px rgba(0,0,0,.4)', animation:'dc-pop .26s cubic-bezier(.34,1.3,.5,1) both' }}>
            <div style={{ width:62, height:62, borderRadius:'50%', margin:'0 auto 18px',
              background:'var(--success)', color:'#fff', display:'flex', alignItems:'center', justifyContent:'center',
              boxShadow:'0 8px 18px rgba(111,142,90,.4)' }}>
              <svg width="30" height="30" viewBox="0 0 24 24">{I.check}</svg>
            </div>
            <div className="t-h3" style={{ fontSize:19 }}>Order Confirmed</div>
            <p className="t-body" style={{ margin:'10px 0 22px', color:'var(--espresso-2)' }}>
              <b>{book.title}</b> is on its way. We’ll deliver it to your address shortly. Thank you for reading with us!
            </p>
            <button className="dc-btn" onClick={()=>{ setDone(false); onNav('books'); }}>BACK TO BOOKS</button>
          </div>
        </div>
      )}
    </div>
  );
}

function StoreCard({ store }){
  const [h, setH] = useState(false);
  const row = (icon, text)=>(
    <div style={{ display:'flex', alignItems:'center', gap:8, marginTop:6 }}>
      <svg width="15" height="15" viewBox="0 0 20 20" style={{ color:'var(--caramel)', flexShrink:0 }}>{I[icon]}</svg>
      <span style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:12, color:'var(--espresso-2)' }}>{text}</span>
    </div>
  );
  return (
    <div onMouseEnter={()=>setH(true)} onMouseLeave={()=>setH(false)}
      style={{ display:'flex', gap:14, background:'var(--surface)', borderRadius:16, padding:12,
        boxShadow: h?'var(--shadow-float)':'var(--shadow-card)', cursor:'pointer',
        transform: h?'translateY(-3px)':'none', outline: h?'2px solid var(--caramel)':'2px solid transparent',
        transition:'all .2s cubic-bezier(.2,.7,.3,1)' }}>
      <div style={{ position:'relative', width:96, height:112, borderRadius:11, flexShrink:0, overflow:'hidden',
        background:`#3a2a1c url(${store.img}) center/cover no-repeat` }}>
        <div style={{ position:'absolute', left:6, top:6, display:'flex', alignItems:'center', gap:3,
          background:'rgba(28,18,10,.62)', backdropFilter:'blur(3px)', borderRadius:'var(--r-pill)', padding:'3px 8px',
          fontFamily:'var(--font-sans)', fontWeight:700, fontSize:8.5, letterSpacing:.5, textTransform:'uppercase', color:'#fff' }}>
          <svg width="9" height="9" viewBox="0 0 20 20" style={{color:'#fff'}}>{I.pin}</svg>{store.city}</div>
      </div>
      <div style={{ flex:1, minWidth:0 }}>
        <div style={{ fontFamily:'var(--font-serif)', fontWeight:700, fontSize:17, color:'var(--espresso)',
          lineHeight:1.2 }}>{store.name}</div>
        <div style={{ marginTop:9 }}>
          {row('pin', store.address)}
          {row('phone', store.contact)}
          {row('clock', store.hours)}
        </div>
      </div>
    </div>
  );
}

function StoresScreen({ onNav }){
  return (
    <div style={{ position:'relative', height:'100%', background:'linear-gradient(180deg, #FFE9CF 0%, #FBE3C6 100%)', overflow:'hidden', display:'flex', flexDirection:'column' }}>
      <div className="noscroll" style={{ flex:1, minHeight:0, overflowY:'auto', paddingBottom:130 }}>
        <Header kicker="Find Us" title="Our Stores" subtitle="Visit us across Indonesia"
          menuKey="stores" onNav={onNav} />
        <div style={{ display:'flex', flexDirection:'column', gap:16, padding:'24px 24px 0' }}>
          {STORES.map(s=> <StoreCard key={s.id} store={s} />)}
        </div>
      </div>
      <BottomNav active="stores" onNav={onNav} />
    </div>
  );
}

Object.assign(window, { DetailScreen, StoreCard, StoresScreen });
})();
