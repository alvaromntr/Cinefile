// tmdb.js — Cliente TMDB com cache e busca inteligente
const TMDB = (() => {
  const API_KEY = "ac239e8cfa775a3f0165865fbab3d463";
  const IMG_BASE = "https://image.tmdb.org/t/p/w342";

  const mem = new Map();
  const lsGet = k => { try { return localStorage.getItem(k); } catch { return null; } };
  const lsSet = (k,v) => { try { localStorage.setItem(k,v); } catch {} };

  const MAX_CONC = 6;
  let active = 0;
  const queue = [];
  const runNext = () => {
    if (!queue.length || active >= MAX_CONC) return;
    active++;
    const { fn, resolve, reject } = queue.shift();
    fn().then(resolve).catch(reject).finally(() => { active--; runNext(); });
  };
  const withQueue = fn => new Promise((resolve,reject)=>{ queue.push({fn,resolve,reject}); runNext(); });

  const keyPoster = (type,id)=>`tmdb_poster_${type}_${id}`;
  const keySearch = (type,title,year)=>`tmdb_search_${type}_${(title||'').toLowerCase()}_${year||''}`;

  async function getPosterById(type,id){
    if(!id) return null;
    const k = keyPoster(type,id);
    if(mem.has(k)) return mem.get(k);
    const fromLS = lsGet(k); if(fromLS){ mem.set(k,fromLS); return fromLS; }
    return withQueue(async()=>{
      const url=`https://api.themoviedb.org/3/${type}/${id}?api_key=${API_KEY}&language=pt-BR`;
      try{ const res = await fetch(url,{cache:"force-cache"}); if(!res.ok) return null;
        const data = await res.json();
        const path = data?.poster_path;
        const full = path ? `${IMG_BASE}${path}` : null;
        if(full){ mem.set(k,full); lsSet(k,full); } return full;
      }catch{return null;}
    });
  }

  async function searchPoster({title,year,type}){
    if(!title) return null;
    const apiType = type === "series" ? "tv" : "movie";
    const k = keySearch(apiType,title,year);
    if(mem.has(k)) return mem.get(k);
    const fromLS = lsGet(k); if(fromLS){ mem.set(k,fromLS); return fromLS; }

    const params = new URLSearchParams({api_key:API_KEY,query:title,include_adult:"false",language:"pt-BR"});
    if(year){ if(apiType==="movie") params.set("year",String(year)); else params.set("first_air_date_year",String(year)); }
    const url = `https://api.themoviedb.org/3/search/${apiType}?${params.toString()}`;

    return withQueue(async()=>{
      try{
        const res = await fetch(url,{cache:"force-cache"});
        if(!res.ok) return null;
        const data = await res.json();
        const best = data?.results?.[0];
        const path = best?.poster_path;
        const full = path ? `${IMG_BASE}${path}` : null;
        if(full){ mem.set(k,full); lsSet(k,full); }
        return full;
      }catch{return null;}
    });
  }

  async function getPosterSmart({tmdbId,typeHere,title,year}){
    const apiType = typeHere==="series"?"tv":"movie";
    if(tmdbId){ const byId = await getPosterById(apiType,tmdbId); if(byId) return byId; }
    if(title) return await searchPoster({title,year,type:typeHere});
    return null;
  }

  return { getPosterById, searchPoster, getPosterSmart, IMG_BASE, API_KEY };
})();
