// screens-auth.jsx — Login / Register
(function(){
const { useState } = React;
const { Logo, TextField, I } = window;

function AuthScreen({ onLogin }){
  const [tab, setTab] = useState('login');          // login | register
  const [u, setU] = useState('');
  const [p, setP] = useState('');
  const [name, setName] = useState('');
  const [err, setErr] = useState({});

  const submit = ()=> onLogin(u.trim() || 'Reader');

  const books = [
    { top:96,  left:300, size:42, rot:-12, op:.5 },
    { top:150, left:344, size:30, rot:14,  op:.4 },
    { top:196, left:300, size:36, rot:-6,  op:.45 },
    { top:128, left:262, size:24, rot:20,  op:.32 },
  ];

  return (
    <div style={{ position:'relative', height:'100%', background:'var(--bg-peach)', overflow:'hidden', display:'flex', flexDirection:'column' }}>
      <div className="noscroll" style={{ flex:1, minHeight:0, overflowY:'auto' }}>
        {/* hero */}
        <div style={{ position:'relative', background:'var(--bg-cream)', padding:'78px 33px 10px' }}>
          {books.map((b,i)=>(
            <img key={i} src="assets/book-float.png" alt="" draggable="false" style={{ position:'absolute',
              top:b.top, left:b.left, width:b.size, opacity:b.op, transform:`rotate(${b.rot}deg)`,
              filter:'saturate(.9)' }}/>
          ))}
          <Logo size={92} />
          <div className="t-h1" style={{ marginTop:14 }}>Hello !</div>
          <div style={{ fontFamily:'var(--font-sans)', fontWeight:700, fontSize:18, color:'var(--black)', marginTop:8 }}>
            {tab==='login' ? 'Welcome back, reader' : 'Join the library'}
          </div>
        </div>

        {/* folder tabs — active tab shares the form colour (Figma) */}
        <div style={{ display:'flex', background:'var(--bg-cream)' }}>
          {[['login','LOGIN'],['register','REGISTER']].map(([id,lbl])=>{
            const on = tab===id;
            return (
              <button key={id} onClick={()=>setTab(id)} style={{
                flex:1, position:'relative', border:'none', cursor:'pointer', padding:'19px 0 20px',
                background: on ? 'var(--bg-peach)' : 'transparent',
                borderTopLeftRadius: on ? 24 : 0,
                borderTopRightRadius: on ? 24 : 0,
                fontFamily:'var(--font-sans)', fontWeight:700, fontSize:16, letterSpacing:.8,
                color: on ? 'var(--espresso)' : 'var(--caramel)',
                transition:'color .22s ease' }}>
                {lbl}
                {on && <span style={{ position:'absolute', left:'50%', top:8, transform:'translateX(-50%)',
                  width:28, height:3, borderRadius:3, background:'var(--coffee)' }}/>}
              </button>
            );
          })}
        </div>

        {/* form */}
        <div key={tab} style={{ padding:'26px 33px 60px', animation:'dc-fade-up .3s ease both' }}>
          {tab==='register' && (
            <TextField label="Full Name" value={name} onChange={v=>{setName(v); setErr({...err,name:null});}}
              placeholder="ex : Jessie Lawrence" error={err.name} />
          )}
          <TextField label="Username" value={u} onChange={v=>{setU(v); setErr({...err,u:null});}}
            placeholder="ex : jessiela" error={err.u} />
          <TextField label="Password" type="password" value={p} onChange={v=>{setP(v); setErr({...err,p:null});}}
            placeholder="ex : j3ss1311" error={err.p} onEnter={submit} />

          <button className="dc-btn" style={{ marginTop:8 }} onClick={submit}>
            {tab==='login' ? 'LOGIN' : 'CREATE ACCOUNT'}
          </button>

          <div style={{ display:'flex', alignItems:'center', gap:12, margin:'22px 0 18px', color:'var(--ink-secondary)' }}>
            <div style={{ flex:1, height:1, background:'rgba(85,53,34,.14)' }}/>
            <span style={{ fontFamily:'var(--font-sans)', fontWeight:600, fontSize:11 }}>or</span>
            <div style={{ flex:1, height:1, background:'rgba(85,53,34,.14)' }}/>
          </div>

          <button onClick={()=>onLogin('Reader')} style={{ width:'100%', height:46, borderRadius:'var(--r-input)',
            border:'1px solid rgba(85,53,34,.16)', background:'var(--surface)', cursor:'pointer',
            display:'flex', alignItems:'center', justifyContent:'center', gap:10,
            fontFamily:'var(--font-sans)', fontWeight:700, fontSize:13.5, color:'var(--espresso)',
            boxShadow:'0 2px 6px rgba(85,53,34,.08)' }}>
            <svg width="18" height="18" viewBox="0 0 20 20">{I.google}</svg>
            Sign in with Google
          </button>

          <div style={{ textAlign:'center', marginTop:22, fontFamily:'var(--font-sans)', fontWeight:600,
            fontSize:11, color:'var(--ink-secondary)' }}>
            Tap {tab==='login' ? 'LOGIN' : 'CREATE ACCOUNT'} to enter the library</div>
        </div>
      </div>
    </div>
  );
}

Object.assign(window, { AuthScreen });
})();
