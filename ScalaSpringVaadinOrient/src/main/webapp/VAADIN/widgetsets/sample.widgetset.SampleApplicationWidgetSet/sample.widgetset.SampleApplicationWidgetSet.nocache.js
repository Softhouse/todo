function sample_widgetset_SampleApplicationWidgetSet(){var O='',vb='" for "gwt:onLoadErrorFn"',tb='" for "gwt:onPropertyErrorFn"',hb='"><\/script>',Y='#',Vb='.cache.html',$='/',Ob='13DC1521D547B4B102D169596004B58F',Pb='1CEEF7F34B3AE696CA4B9008F7418426',Qb='3DDF3A8AE77356ABCD043CA682C77A28',Rb='4067376B49416EBB70EC30E7D04132D1',Sb='6BD3A8BA2FED195BECB346EDC7D8432B',Ub=':',nb='::',cc='<script defer="defer">sample_widgetset_SampleApplicationWidgetSet.onInjectionDone(\'sample.widgetset.SampleApplicationWidgetSet\')<\/script>',gb='<script id="',qb='=',Z='?',Tb='BD75101035F0BCEABC20F7D1A1D3B8AD',sb='Bad handler "',bc='DOMContentLoaded',ib='SCRIPT',fb='__gwt_marker_sample.widgetset.SampleApplicationWidgetSet',jb='base',bb='baseUrl',S='begin',R='bootstrap',ab='clear.cache.gif',pb='content',X='end',ac='filtertable/filtertable.css',Ib='gecko',Jb='gecko1_8',T='gwt.codesvr=',U='gwt.hosted=',V='gwt.hybrid',ub='gwt:onLoadErrorFn',rb='gwt:onPropertyErrorFn',ob='gwt:property',_b='head',Mb='hosted.html?sample_widgetset_SampleApplicationWidgetSet',$b='href',Hb='ie6',Gb='ie8',Fb='ie9',wb='iframe',_='img',xb="javascript:''",Xb='link',Lb='loadExternalRefs',kb='meta',zb='moduleRequested',W='moduleStartup',Eb='msie',lb='name',Bb='opera',Wb='popupbutton/popupbutton.css',yb='position:absolute;width:0;height:0;border:none',Yb='rel',Db='safari',P='sample.widgetset.SampleApplicationWidgetSet',db='sample.widgetset.SampleApplicationWidgetSet.nocache.js',mb='sample.widgetset.SampleApplicationWidgetSet::',cb='script',Nb='selectingPermutation',Q='startup',Zb='stylesheet',eb='undefined',Kb='unknown',Ab='user.agent',Cb='webkit';var l=window,m=document,n=l.__gwtStatsEvent?function(a){return l.__gwtStatsEvent(a)}:null,o=l.__gwtStatsSessionId?l.__gwtStatsSessionId:null,p,q,r,s=O,t={},u=[],v=[],w=[],x=0,y,z;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:S});if(!l.__gwt_stylesLoaded){l.__gwt_stylesLoaded={}}if(!l.__gwt_scriptsLoaded){l.__gwt_scriptsLoaded={}}function A(){var b=false;try{var c=l.location.search;return (c.indexOf(T)!=-1||(c.indexOf(U)!=-1||l.external&&l.external.gwtOnLoad))&&c.indexOf(V)==-1}catch(a){}A=function(){return b};return b}
function B(){if(p&&q){var b=m.getElementById(P);var c=b.contentWindow;if(A()){c.__gwt_getProperty=function(a){return G(a)}}sample_widgetset_SampleApplicationWidgetSet=null;c.gwtOnLoad(y,P,s,x);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:X})}}
function C(){function e(a){var b=a.lastIndexOf(Y);if(b==-1){b=a.length}var c=a.indexOf(Z);if(c==-1){c=a.length}var d=a.lastIndexOf($,Math.min(c,b));return d>=0?a.substring(0,d+1):O}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=m.createElement(_);b.src=a+ab;a=e(b.src)}return a}
function g(){var a=E(bb);if(a!=null){return a}return O}
function h(){var a=m.getElementsByTagName(cb);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(db)!=-1){return e(a[b].src)}}return O}
function i(){var a;if(typeof isBodyLoaded==eb||!isBodyLoaded()){var b=fb;var c;m.write(gb+b+hb);c=m.getElementById(b);a=c&&c.previousSibling;while(a&&a.tagName!=ib){a=a.previousSibling}if(c){c.parentNode.removeChild(c)}if(a&&a.src){return e(a.src)}}return O}
function j(){var a=m.getElementsByTagName(jb);if(a.length>0){return a[a.length-1].href}return O}
var k=g();if(k==O){k=h()}if(k==O){k=i()}if(k==O){k=j()}if(k==O){k=e(m.location.href)}k=f(k);s=k;return k}
function D(){var b=document.getElementsByTagName(kb);for(var c=0,d=b.length;c<d;++c){var e=b[c],f=e.getAttribute(lb),g;if(f){f=f.replace(mb,O);if(f.indexOf(nb)>=0){continue}if(f==ob){g=e.getAttribute(pb);if(g){var h,i=g.indexOf(qb);if(i>=0){f=g.substring(0,i);h=g.substring(i+1)}else{f=g;h=O}t[f]=h}}else if(f==rb){g=e.getAttribute(pb);if(g){try{z=eval(g)}catch(a){alert(sb+g+tb)}}}else if(f==ub){g=e.getAttribute(pb);if(g){try{y=eval(g)}catch(a){alert(sb+g+vb)}}}}}}
function E(a){var b=t[a];return b==null?null:b}
function F(a,b){var c=w;for(var d=0,e=a.length-1;d<e;++d){c=c[a[d]]||(c[a[d]]=[])}c[a[e]]=b}
function G(a){var b=v[a](),c=u[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(z){z(a,d,b)}throw null}
var H;function I(){if(!H){H=true;var a=m.createElement(wb);a.src=xb;a.id=P;a.style.cssText=yb;a.tabIndex=-1;m.body.appendChild(a);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:W,millis:(new Date).getTime(),type:zb});a.contentWindow.location.replace(s+K)}}
v[Ab]=function(){var b=navigator.userAgent.toLowerCase();var c=function(a){return parseInt(a[1])*1000+parseInt(a[2])};if(function(){return b.indexOf(Bb)!=-1}())return Bb;if(function(){return b.indexOf(Cb)!=-1}())return Db;if(function(){return b.indexOf(Eb)!=-1&&m.documentMode>=9}())return Fb;if(function(){return b.indexOf(Eb)!=-1&&m.documentMode>=8}())return Gb;if(function(){var a=/msie ([0-9]+)\.([0-9]+)/.exec(b);if(a&&a.length==3)return c(a)>=6000}())return Hb;if(function(){return b.indexOf(Ib)!=-1}())return Jb;return Kb};u[Ab]={gecko1_8:0,ie6:1,ie8:2,ie9:3,opera:4,safari:5};sample_widgetset_SampleApplicationWidgetSet.onScriptLoad=function(){if(H){q=true;B()}};sample_widgetset_SampleApplicationWidgetSet.onInjectionDone=function(){p=true;n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:Lb,millis:(new Date).getTime(),type:X});B()};D();C();var J;var K;if(A()){if(l.external&&(l.external.initModule&&l.external.initModule(P))){l.location.reload();return}K=Mb;J=O}n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:Nb});if(!A()){try{F([Gb],Ob);F([Hb],Pb);F([Fb],Qb);F([Bb],Rb);F([Db],Sb);F([Jb],Tb);J=w[G(Ab)];var L=J.indexOf(Ub);if(L!=-1){x=Number(J.substring(L+1));J=J.substring(0,L)}K=J+Vb}catch(a){return}}var M;function N(){if(!r){r=true;if(!__gwt_stylesLoaded[Wb]){var a=m.createElement(Xb);__gwt_stylesLoaded[Wb]=a;a.setAttribute(Yb,Zb);a.setAttribute($b,s+Wb);m.getElementsByTagName(_b)[0].appendChild(a)}if(!__gwt_stylesLoaded[ac]){var a=m.createElement(Xb);__gwt_stylesLoaded[ac]=a;a.setAttribute(Yb,Zb);a.setAttribute($b,s+ac);m.getElementsByTagName(_b)[0].appendChild(a)}B();if(m.removeEventListener){m.removeEventListener(bc,N,false)}if(M){clearInterval(M)}}}
if(m.addEventListener){m.addEventListener(bc,function(){I();N()},false)}var M=setInterval(function(){if(/loaded|complete/.test(m.readyState)){I();N()}},50);n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:R,millis:(new Date).getTime(),type:X});n&&n({moduleName:P,sessionId:o,subSystem:Q,evtGroup:Lb,millis:(new Date).getTime(),type:S});m.write(cc)}
sample_widgetset_SampleApplicationWidgetSet();