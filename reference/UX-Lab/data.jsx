// data.jsx — catalogue + stores for BOOWORM
// Covers are styled placeholders: a clothbound spine in a classic tone with
// gold (caramel) foil title/author. No external cover images.
(function(){

const BOOKS = [
  // ── Non-fiction (philosophy / self-improvement) ──
  { id:'meditations', title:'Meditations', author:'Marcus Aurelius', cat:'Non-Fiction', tag:'Stoic Philosophy',
    cloth:'#2E5248', accent:'#E7C48B', year:'c. 180', pages:254, price:95000,
    blurb:'The private notes of a Roman emperor to himself — a timeless manual on discipline, perspective and living with virtue amid chaos.' },
  { id:'republic', title:'The Republic', author:'Plato', cat:'Non-Fiction', tag:'Classical Thought',
    cloth:'#6E2B2B', accent:'#E7C48B', year:'c. 375 BC', pages:416, price:110000,
    blurb:'Plato’s dialogue on justice, the ideal city and the nature of the good — the cornerstone of Western political philosophy.' },
  { id:'walden', title:'Walden', author:'Henry David Thoreau', cat:'Non-Fiction', tag:'Reflection',
    cloth:'#3B5230', accent:'#E7C48B', year:'1854', pages:352, price:89000,
    blurb:'Two years, two months and two days at Walden Pond — a meditation on simplicity, self-reliance and the natural world.' },
  { id:'seneca', title:'Letters from a Stoic', author:'Seneca', cat:'Non-Fiction', tag:'Stoic Philosophy',
    cloth:'#27384F', accent:'#E7C48B', year:'c. 65', pages:254, price:92000,
    blurb:'Warm, practical letters on friendship, mortality and tranquillity — Stoic wisdom that still reads like advice from a wise friend.' },

  // ── Fiction (classic literature) ──
  { id:'pride', title:'Pride and Prejudice', author:'Jane Austen', cat:'Fiction', tag:'Classic Romance',
    cloth:'#7C4A63', accent:'#EBD0AE', year:'1813', pages:432, price:99000,
    blurb:'Elizabeth Bennet and Mr Darcy spar their way toward love in Austen’s sparkling comedy of manners, class and first impressions.' },
  { id:'gatsby', title:'The Great Gatsby', author:'F. Scott Fitzgerald', cat:'Fiction', tag:'Jazz Age',
    cloth:'#1F2B47', accent:'#E7C48B', year:'1925', pages:180, price:89000,
    blurb:'A bootlegger’s pursuit of a green light across the bay — Fitzgerald’s shimmering elegy for the American dream.' },
  { id:'janeeyre', title:'Jane Eyre', author:'Charlotte Brontë', cat:'Fiction', tag:'Gothic',
    cloth:'#5A2E33', accent:'#E7C48B', year:'1847', pages:532, price:115000,
    blurb:'An orphan governess, a brooding master and a secret in the attic — Brontë’s fierce story of conscience and independence.' },
  { id:'moby', title:'Moby-Dick', author:'Herman Melville', cat:'Fiction', tag:'Adventure',
    cloth:'#374A54', accent:'#E7C48B', year:'1851', pages:720, price:129000,
    blurb:'Call me Ishmael. Captain Ahab’s obsessive hunt for the white whale becomes a vast meditation on fate, nature and madness.' },
];

const FEATURED = ['pride','meditations','gatsby','republic'];

const STORES = [
  { id:'ivory-tower', name:'Ivory Tower Books', img:'assets/store-1.png', city:'Bandung',
    address:'Jl. Braga No. 18, Bandung', contact:'+62 812 3344 5566', hours:'09.00 – 21.00' },
  { id:'platos', name:'Plato’s Atheneum', img:'assets/store-2.png', city:'Yogyakarta',
    address:'Jl. Malioboro No. 52, Yogyakarta', contact:'+62 877 1122 3344', hours:'10.00 – 22.00' },
  { id:'serpents', name:'The Serpent’s Archive', img:'assets/store-4.png', city:'Jakarta',
    address:'Jl. Kemang Raya No. 7, Jakarta', contact:'+62 813 9988 7766', hours:'08.00 – 20.00' },
  { id:'alexandria', name:'Alexandria Branch', img:'assets/store-5.png', city:'Surabaya',
    address:'Jl. Tunjungan No. 31, Surabaya', contact:'+62 856 4455 6677', hours:'09.00 – 21.00' },
];

const CAROUSEL = STORES.map(s => ({ img:s.img, name:s.name, city:s.city }));

const bookById = (id) => BOOKS.find(b => b.id === id);
const rupiah = (n) => 'Rp ' + Math.round(n).toLocaleString('id-ID');

Object.assign(window, { BOOKS, FEATURED, STORES, CAROUSEL, bookById, rupiah });
})();
