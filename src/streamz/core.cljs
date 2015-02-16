(ns streamz.core
  (:require cljs.reader
            [figwheel.client :as fw]
            [reagent.core :as r]
            [reagent.cursor :as rc]
            [reagent-forms.core :as rf]))

(def storage-ver 1)
(def local-storage-key "streamzCljData")

(def streams-data
  [{:name "Telegraph"
    :type :iframe
    :src "http://player.ooyala.com/iframe.html#ec=42Njdmazp8OPIKJRyChESz2RevNqe2aQ&pbid=ZTIxYmJjZDM2NWYzZDViZGRiOWJjYzc5&docUrl=http://streamzdude.github.io/streamz/"}
   {:name "RT"
    :type :jwplayer
    :src "rtmp://fml.3443.edgecastcdn.net/203443/en-stream"
    :bufferlength 20}
   {:name "Reuters"
    :type :jwplayer
    :src "http://37.58.85.156/rlo001/ngrp:rlo001.stream_all/playlist.m3u8"
    :image false
    :bufferlength 20}
   {:name "Walla"
    :type :jwplayer
    :src "rtmp://waflalive.walla.co.il/livestreamcast_edge?tuid=23688935517444&un=&ait=129&wkeys=2689&divname=.videochannel&provider=WallaNewsDesk&location=walla.news.&channel_name=news/news"
    :bufferlength 7}
   {:name "Ch 2"
    :type :iframe
    :src "http://www.ustream.tv/embed/19798681?v=3&amp;wmode=direct"}
   {:name "Ch 10"
    :type :html
    :src "<object class=\"CTPlayer\" type=\"application/x-shockwave-flash\" data=\"http://hlslive.ch10.cloudvideoplatform.com/CTMLivePlayer.swf?0.9139183044899255\" width=\"100%\" height=\"100%\" title=\"Ch10\"><param name=\"quality\" value=\"high\"> <param name=\"bgcolor\" value=\"#000000\"><param name=\"allowScriptAccess\" value=\"always\"> <param name=\"wmode\" value=\"direct\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"flashvars\" value=\"AutoPlay=true&amp;dfpadunit=CDN_10TV&amp;PathToPlayer=http://hlslive.ch10.cloudvideoplatform.com/\"></object>"}
   {:name "CNN"
    :type :html
    :src "<object type=\"application/x-shockwave-flash\" height=\"100%\" width=\"100%\" data=\"http://fpdownload.adobe.com/strobe/FlashMediaPlayback_101.swf\"><param name=\"data\" value=\"http://fpdownload.adobe.com/strobe/FlashMediaPlayback_101.swf\"><param name=\"flashvars\" value=\"src=http://wpc.C1A9.edgecastcdn.net/hds-live/20C1A9/cnn/ls_satlink/b_828.f4m&#038;streamType=live&#038;autoPlay=true&#038;controlBarAutoHide=true\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"allowscriptaccess\" value=\"always\"><param name=\"src\" value=\"http://fpdownload.adobe.com/strobe/FlashMediaPlayback_101.swf\"><param name=\"allowfullscreen\" value=\"true\"></object>"
    :width 606
    :height 418}
   {:name "CNN USA"
    :type :iframe
    :src "http://embed.a1livetv.com/cnn-usa.php"
    :width 664
    :height 468}
   {:name "BBC"
    :type :jwplayer
    :src "http://wpc.c1a9.edgecastcdn.net/hls-live/20C1A9/bbc_world/ls_satlink/b_828.m3u8"
    :bufferlength 6}
   {:name "Sky News"
    :type :iframe
    :src "http://www.youtube.com/embed/VYlQJbsVs48?rel=0"
    :visible false}
   {:name "Aljazeera"
    :type :html
    :src "<object seamlesstabbing=\"undefined\" class=\"BrightcoveExperience\" id=\"myExperience1474541474001\" data=\"http://c.brightcove.com/services/viewer/federated_f9?&amp;width=680&amp;height=380&amp;flashID=myExperience1474541474001&amp;bgcolor=%23FFFFFF&amp;playerID=834025853001&amp;playerKey=AQ~~%2CAAAAmtUu4Zk~%2CoARQlkfrZncis5e3VirW1_jMMBOC_SsO&amp;isVid=true&amp;isUI=true&amp;dynamicStreaming=true&amp;%40videoPlayer=1474541474001&amp;autoStart=true&amp;debuggerID=&amp;startTime=1405645205983\" type=\"application/x-shockwave-flash\" height=\"100%\" width=\"100%\"><param value=\"always\" name=\"allowScriptAccess\"><param value=\"true\" name=\"allowFullScreen\"><param value=\"false\" name=\"seamlessTabbing\"><param value=\"true\" name=\"swliveconnect\"><param value=\"window\" name=\"wmode\"><param value=\"high\" name=\"quality\"><param value=\"#000000\" name=\"bgcolor\"></object>"
    :visible false}
   {:name "Al Aqsa"
    :type :jwplayer
    :src "http://live.aqsatv.atyaf.co:1935/aqsatv/live/tv/playlist.m3u8"
    :bufferlength 60
    :visible false}
   {:name "Al-Manar"
    :type :jwplayer
    :src "rtmp://38.96.148.204:1935/liveedge/livestream_360p"
    :bufferlength 5
    :visible false}
   {:name "i24"
    :type :html
    :src "<object width=\"100%\" height=\"100%\" type=\"application/x-shockwave-flash\" data=\"http://c.brightcove.com/services/viewer/federated_f9?&amp;width=470&amp;height=350&amp;flashID=myExperience2552000984001&amp;bgcolor=%23000000&amp;playerID=2551661482001&amp;playerKey=AQ~~%2CAAACL1AyZ1k~%2ChYvoCrzvEtsjnBzkMXyn0g7qGNI0eDJy&amp;isVid=true&amp;isUI=true&amp;autoStart=true&amp;htmlFallback=true&amp;dynamicStreaming=true&amp;%40videoPlayer=2552000984001&amp;includeAPI=true&amp;templateLoadHandler=onTemplateLoad&amp;templateReadyHandler=brightcove%5B%22templateReadyHandlermyExperience2552000984001%22%5D&amp;debuggerID=&amp;originalTemplateReadyHandler=onTemplateReady&amp;startTime=1396304197501\" id=\"myExperience2552000984001\" class=\"BrightcoveExperience\" seamlesstabbing=\"undefined\"><param name=\"allowScriptAccess\" value=\"always\"><param name=\"allowFullScreen\" value=\"true\"><param name=\"seamlessTabbing\" value=\"false\"><param name=\"swliveconnect\" value=\"true\"><param name=\"wmode\" value=\"window\"><param name=\"quality\" value=\"high\"><param name=\"bgcolor\" value=\"#000000\"></object>"
    :visible false}
   {:name "PressTV"
    :type :jwplayer
    :src "http://ptv-hls.streaming.overon.es/channel03/livehigh.m3u8"
    :bufferlength 10
    :visible false}
   {:name "RT News"
    :type :jwplayer
    :src "http://odna.octoshape.net/f3f5m2v4/cds/smil:ch1_auto.smil/playlist.m3u8"
    :bufferlength 8
    :visible false}
   {:name "Ukraine Today"
    :type :iframe
    :src "http://www.youtube.com/embed/MeKNjoegIZ4?rel=0"
    :visible false}
   {:name "CBS"
    :type :jwplayer
    :src "rtmp://cp56371.live.edgefcs.net:443/live?ovpfv=1.1/CBSnews2_live_800@50979"
    :bufferlength 20}
   {:name "CBC"
    :type :html
    :src "<object type=\"application/x-shockwave-flash\" data=\"http://pdk.theplatform.com/current/pdk/swf/flvPlayer.swf\" id=\"_playercp88691PdkSwfObject\" name=\"_playercp88691PdkSwfObject\" width=\"100%\" height=\"100%\" wmode=\"opaque\" allowfullscreen=\"true\"><param name=\"scale\" value=\"noscale\"><param name=\"flashvars\" value=\"id=playercp88691&amp;skinurl=http%3A%2F%2Fpdk.theplatform.com%2Fcurrent%2Fpdk%2Fskins%2Fglass%2Fglass.json&amp;usebootloader=true&amp;backgroundcolor=0x131313&amp;controlbackgroundcolor=0x131313&amp;controlcolor=0xffffff&amp;controlframecolor=0x000000&amp;controlhovercolor=0xffffff&amp;controlselectedcolor=0xdc241f&amp;framecolor=0x000000&amp;loadprogresscolor=0xffffff&amp;pagebackgroundcolor=0x131313&amp;playprogresscolor=0xdc241f&amp;scrubtrackcolor=0x7b8582&amp;scrubbercolor=0xffffff&amp;scrubberframecolor=0xdc241f&amp;textbackgroundcolor=0x131313&amp;textcolor=0xffffff&amp;allowfullscreen=true&amp;wmode=opaque&amp;supportedmedia=mpeg4%2Cf4m%2Cflv%2Cm3u%2Cogg%2Cwebm%2Cmpeg%2Cqt%2C3gpp%2Cism%2Cwm%2C3gpp2%2Caac%2Casx%2Cavi%2Cmove%2Cmp3%2Cmpeg-dash&amp;releaseurlformatresolution=true&amp;usedefaultcards=false&amp;scopes=cp88691&amp;shownav=false&amp;allowrss=false&amp;layout=%3C%3Fxml+version%3D%221.0%22%3F%3E%0A%3Clayout%3E%0A++++%3Ccontrols%3E%0A++++++++%3Ccard+id%3D%22tpAdvertisementControls%22%3E%0A++++++++++++%3Cregion+id%3D%22tpTitleRegion%22%3E%0A++++++++++++++++%3Crow+id%3D%22tpTitleContainer%22+percentWidth%3D%22100%22%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpTitle%22%2F%3E%0A++++++++++++++++%3C%2Frow%3E%0A++++++++++++%3C%2Fregion%3E%0A++++++++++++%3Cregion+id%3D%22tpAdCountdownRegion%22%3E%0A++++++++++++++++%3Crow+id%3D%22tpAdCountdownContainer%22+percentWidth%3D%22100%22%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpTimeDivider%22+text%3D%22Your+content+will+play+after+these+messages.%22+%2F%3E++++++++++++++++%0A++++++++++++++++%3C%2Frow%3E%0A++++++++++++%3C%2Fregion%3E%0A++++++++++++%3Cregion+id%3D%22tpBottomRegion%22+alpha%3D%2275%22%3E%0A++++++++++++++++%3Crow%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpPlay%22%2F%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpVolume%22%2F%3E%0A++++++++++++++++++++%3Cgroup+percentWidth%3D%22100%22%3E%0A++++++++++++++++++++++++%3Ccontrol+id%3D%22tpScrubber%22+%2F%3E%0A++++++++++++++++++++++++%3Cspacer%2F%3E%0A++++++++++++++++++++++++%3Ccontrol+id%3D%22tpCurrentTime%22%2F%3E%0A++++++++++++++++++++++++%3Ccontrol+id%3D%22tpTimeDivider%22%2F%3E%0A++++++++++++++++++++++++%3Ccontrol+id%3D%22tpTotalTime%22%2F%3E%0A++++++++++++++++++++++++%3Cspacer%2F%3E%0A++++++++++++++++++++%3C%2Fgroup%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpCC%22+tooltipEnabled%3D%22true%22++%2F%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpFullScreen%22%2F%3E%0A++++++++++++++++%3C%2Frow%3E%0A++++++++++++%3C%2Fregion%3E%0A++++++++%3C%2Fcard%3E%0A++++++++%3Ccard+id%3D%22tpNormalControls%22%3E%0A++++++++++++%3Cregion+id%3D%22tpTitleRegion%22%3E%0A++++++++++++++++%3Crow+id%3D%22tpTitleContainer%22+percentWidth%3D%22100%22%3E%0A++++++++++++++++++++%3Ccontrol+id%3D%22tpTitle%22%2F%3E%0A++++++++++++++++%3C%2Frow%3E%0A++++++++++++%3C%2Fregion%3E%0A%0A++++++++++++%3Cregion+id%3D%22tpBottomFloatRegion%22+alpha%3D%2275%22%3E%0A%09%09%09%3Crow%3E%0A%09%09%09%09%3Ccontrol+id%3D%22tpPlay%22%2F%3E%0A%09%09%09%09%3Ccontrol+id%3D%22tpVolume%22%2F%3E%0A%09%09%09%09%3Cspacer+percentWidth%3D%2280%22%2F%3E%0A%09%09%09%09%3Cgroup+percentWidth%3D%22100%25%22%3E%0A%09%09%09%09%3Cspacer%2F%3E%0A%09%09%09%09%09%3Ccontrol+id%3D%22tpCurrentTime%22%2F%3E%0A%09%09%09%09%09%3Ccontrol+id%3D%22tpTimeDivider%22+text%3D%22%2F+LIVE%22+%2F%3E%0A%09%09%09%09%09%3Cspacer%2F%3E%0A%09%09%09%09%3C%2Fgroup%3E%0A%09%09%09%09%3Ccontrol+id%3D%22tpCC%22+tooltipEnabled%3D%22true%22++%2F%3E%0A%09%09%09%09%3Ccontrol+id%3D%22tpFullScreen%22%2F%3E%0A%09%09%09%3C%2Frow%3E%0A%09%09%3C%2Fregion%3E%0A++++++++%3C%2Fcard%3E%0A++++%3C%2Fcontrols%3E%0A%3C%2Flayout%3E&amp;releaseurl=http%3A%2F%2Flink.theplatform.com%2Fs%2Fh9dtGB%2F5uMiqz53X3EPuRdo0zTjbRYbSX8AEHTm%3Fmbr%3Dtrue%26format%3DSMIL%26player%3Ddefault-prod-live%26policy%3D13951&amp;autoplay=true&amp;relateditemsurl=http%3A%2F%2Ffeed.theplatform.com%2Ff%2Fh9dtGB%2Fb4WWO4mWydul%3FbyRelatedReleasePid%3D%7BreleasePid%7D%26params%3Dmbr%253Dtrue%2526format%253DSMIL&amp;allowlink=false&amp;playerurl=http%3A%2F%2Fplayer.theplatform.com%2Fp%2Fh9dtGB%2FhWZjPZAw3l5W%2Fselect%2F%7BreleasePid%7D%3FvideoWidth%3D620%26videoHeight%3D348%26autoPlay%3Dtrue%26instance%3Dcp88691&amp;showfulltime=true&amp;controlhighlightcolor=0x242424&amp;pdk=next&amp;usenativecontrols=true&amp;endcard=none&amp;plugin1=type%3Dcaptions%7Curl%3Dhttp%3A%2F%2Fpdk.theplatform.com%2Fcurrent%2Fpdk%2Fswf%2FliveCaptions.swf%7Cpriority%3D2%7Calign%3Dbottom&amp;plugin2=type%3Ddelivery%7Curl%3Dhttp%3A%2F%2Flivepassdl.conviva.com%2FthePlatform%2Fc3.CBC-English%2FConvivaThePlatformPlugin_5_0_5.swf%7CcustomerName%3Dc3.CBC-English%7CmetadataKeys%3DcontentArea%252CaudioVideo%252Ccategory%252CliveOndemand%252Cshow%252Ctitle%252CairDay%252Csport%252Cshow%252CseasonNumber%252Cregion%252Cgenre%252CepisodeNumber%252Ctype%7Cfallback%3Dswitch%253Dhttp%7CcustomerId%3Dc3.CBC-English%7CauthAtConnect%3Dtrue%7Cpriority%3D1%7Cmanifest%3Dtrue%7CserviceUrl%3Dhttp%253A%252F%252Flivepass.conviva.com%7CandroidHLS%3Dtrue%7CpluginType%3Dprecision%7CauthMode%3D1&amp;plugin3=type%3Ddelivery%7Curl%3Dhttp%3A%2F%2Flivepassdl.conviva.com%2FthePlatform%2Fc3.CBC-English%2FConvivaThePlatformPlugin.js%7CcustomerName%3Dc3.CBC-English%7CmetadataKeys%3DcontentArea%252CaudioVideo%252Ccategory%252CliveOndemand%252Cshow%252Ctitle%252CairDay%252Csport%252Cshow%252CseasonNumber%252Cregion%252Cgenre%252CepisodeNumber%252Ctype%7Cfallback%3Dswitch%253Dhttp%7CcustomerId%3Dc3.CBC-English%7CauthAtConnect%3Dtrue%7Cpriority%3D1%7Cmanifest%3Dtrue%7CserviceUrl%3Dhttp%253A%252F%252Flivepass.conviva.com%7CandroidHLS%3Dtrue%7CpluginType%3Dprecision%7CauthMode%3D1&amp;plugin4=type%3Danalyticscomponent%7Curl%3Dhttp%3A%2F%2F79423.analytics.edgesuite.net%2Fcsma%2Fpdk%2FPDKCSMALoader.swf%7CpluginPath%3Dhttp%253A%252F%252F79423.analytics.edgesuite.net%252Fcsma%252Fplugin%252Fcsma.swf%7CconfigPath%3Dhttp%253A%252F%252Fma80-r.analytics.edgesuite.net%252Fconfig%252Fbeacon-5713.xml&amp;plugin5=type%3Dtracking%7Curl%3Dhttp%3A%2F%2Fsb.scorecardresearch.com%2Fc2%2Fplugins%2Fstreamsense_plugin_theplatform.swf%7Clabelmapping%3Dns_st_ep%253D%2522S%2522%252BseasonNumber%252B%2522E%2522%252BepisodeNumber%252Cc6%253Dshow%252B%2522%257C%2522%252Bclip.title%252B%2522%257C%2522%252BepisodeNumber%252B%2522%257C%2522seasonNumber%7Cc2%3D16433048%7Cpriority%3D3%7Cpersistentlabels%3Dc3%253DCBC&amp;plugin6=type%3Dtracking%7Curl%3Dhttp%3A%2F%2Fsb.scorecardresearch.com%2Fc2%2Fplugins%2Fstreamsense_plugin_theplatform.js%7Clabelmapping%3Dns_st_ep%253D%2522S%2522%252BseasonNumber%252B%2522E%2522%252BepisodeNumber%252Cc6%253Dshow%252B%2522%257C%2522%252Bclip.title%252B%2522%257C%2522%252BepisodeNumber%252B%2522%257C%2522seasonNumber%7Cc2%3D16433048%7Cpriority%3D3%7Cpersistentlabels%3Dc3%253DCBC&amp;plugin7=type%3Dtracking%7Curl%3Dhttp%3A%2F%2Fmain.mp4.cbc.ca%2F%2FprodVideo%2Fplugins%2FComScorePlugin.swf%7Cc2%3Dnull%7Cpriority%3D4%7CminLongformDuration%3DNaN%7Cc4%3Dnull&amp;plugin8=type%3Dad%7Curl%3Dhttp%3A%2F%2Fpdk.theplatform.com%2Fnext%2Fpdk%2Fswf%2Fdoubleclick.swf%7Chost%3Dpubads.g.doubleclick.net%7Cpriority%3D1&amp;plugin9=type%3Dad%7Curl%3Dhttp%3A%2F%2Fpdk.theplatform.com%2Fnext%2Fpdk%2Fjs%2Fplugins%2Fdoubleclick.js%7Chost%3Dpubads.g.doubleclick.net%7Cpriority%3D1&amp;videoengineruntime=flash&amp;formats=mpeg4%2Cf4m%2Cflv%2Cmp3&amp;\"><param name=\"allowscriptaccess\" value=\"always\"><param name=\"allowfullscreeninteractive\" value=\"false\"><param name=\"menu\" value=\"true\"><param name=\"salign\" value=\"tl\"><param name=\"wmode\" value=\"opaque\"><param name=\"allowfullscreen\" value=\"true\"><param name=\"bgcolor\" value=\"#131313\"></object>"
    :visible false}
   {:name "ynet"
    :type :html
    :src "<object width=\"100%\" height=\"100%\" id=\"player_api\" data=\"http://mediadownload.ynet.co.il/zeri/flowplayer.commercial-3.2.7.swf\" type=\"application/x-shockwave-flash\"><param name=\"allowfullscreen\" value=\"true\"><param name=\"allowscriptaccess\" value=\"always\"><param name=\"quality\" value=\"high\"><param name=\"cachebusting\" value=\"false\"><param name=\"bgcolor\" value=\"#000000\"><param name=\"flashvars\" value=\"config={&quot;debug&quot;:false,&quot;key&quot;:&quot;#$82d88990cf33aa2235a&quot;,&quot;logo&quot;:{&quot;url&quot;:&quot;http://mediadownload.ynet.co.il/flowplayerlive/logoynetlive.png&quot;,&quot;top&quot;:10,&quot;left&quot;:10,&quot;fullscreenOnly&quot;:false,&quot;zIndex&quot;:11},&quot;plugins&quot;:{&quot;controls&quot;:{&quot;url&quot;:&quot;http://mediadownload.ynet.co.il/zeri/flowplayer.controls-3.2.5.swf&quot;,&quot;time&quot;:false},&quot;akamai&quot;:{&quot;url&quot;:&quot;http://players.edgesuite.net/flash/plugins/flow/v2.11/AkamaiFlowPlugin.swf&quot;},&quot;gatracker&quot;:{&quot;url&quot;:&quot;http://mediadownload.ynet.co.il/flowplayerlive/flowplayer.analytics-3.2.2.swf&quot;,&quot;events&quot;:{&quot;all&quot;:true},&quot;accountId&quot;:&quot;UA-17826742-7&quot;},&quot;hiro&quot;:{&quot;url&quot;:&quot;http://mediadownload.ynet.co.il/flowplayerlive/Flowplayer_Hiro_Ynet_2_9_0_15409.swf&quot;,&quot;flavor&quot;:&quot;YNET&quot;,&quot;site_id&quot;:&quot;9998&quot;}},&quot;clip&quot;:{&quot;url&quot;:&quot;http://ynet-lh.akamaihd.net/z/ynet_1@123292/manifest.f4m&quot;,&quot;provider&quot;:&quot;akamai&quot;,&quot;live&quot;:true,&quot;eventCategory&quot;:&quot;2015/01/07-16:05-News/National/General_Info-Other-snow&quot;},&quot;playerId&quot;:&quot;player&quot;,&quot;playlist&quot;:[{&quot;url&quot;:&quot;http://ynet-lh.akamaihd.net/z/ynet_1@123292/manifest.f4m&quot;,&quot;provider&quot;:&quot;akamai&quot;,&quot;live&quot;:true,&quot;eventCategory&quot;:&quot;2015/01/07-16:05-News/National/General_Info-Other-snow&quot;}]}\"></object>"}
   {:name "TV2"
    :type :jwplayer
    :src "http://hls.akamai.tv2.no/wzlive/_definst_/amlst:WS26/831749.smil/manifest.m3u8"
    :bufferlength 10
    :visible false}
   {:name "AA"
    :type :iframe
    :src "http://static.str.noccdn.net/tvkur.com/aa/live1.html"
    :visible false}
   {:name "Jehad"
    :type :iframe
    :src "http://www.ustream.tv/embed/12632661?v=3&amp;wmode=direct"
    :visible false}
   {:name "Cats"
    :type :iframe
    :src "http://www.ustream.tv/embed/2298576?v=3&amp;wmode=direct"}
   {:name "Chat"
    :type :html
    :src "<script id=\"cid0020000066695717479\" src=\"//st.chatango.com/js/gz/emb.js\" async style=\"width:100%;height:100%;\">{\"handle\":\"happeningmonitor\",\"arch\":\"js\",\"styles\":{\"a\":\"cc0000\",\"b\":100,\"c\":\"FFFFFF\",\"d\":\"FFFFFF\",\"k\":\"cc0000\",\"l\":\"cc0000\",\"m\":\"cc0000\",\"n\":\"FFFFFF\",\"q\":\"cc0000\",\"r\":100,\"t\":0,\"usricon\":0.99,\"allowpm\":0}}</script>"}])

(defn normalize-streams [streams]
  (mapv
   (fn [stream idx]
     (assoc stream
       :idx idx
       :visible (:visible stream true)))
   streams (range)))

(defonce app-state
  (r/atom {:is-customizing false
           :show-reload true
           :show-overlays false
           :edit {:is-editing false, :name "", :stream-type :iframe, :src ""}
           :streams (normalize-streams streams-data)}))


(defn pathify [path] (if (sequential? path) path [path]))

(defn update-val! [path f] (swap! app-state update-in (pathify path) f))

(defn toggle! [path] (update-val! path not))

(defn set-val! [& pathvals]
  (reset! app-state (reduce
                     (fn [state [path val]] (assoc-in state (pathify path) val))
                     @app-state
                     (partition 2 pathvals))))

(defn save []
  (->> {:ver storage-ver
        :state @app-state}
       pr-str
       (aset js/localStorage local-storage-key)))

(defonce loaded (atom false))

(defn load [data]
  (reset! loaded true)
  (set-val! :show-reload (:show-reload data true)
            :streams (normalize-streams
                      (map (fn [{:keys [name] :as stream}]
                             (merge stream (first (filter
                                                   #(= name (:name %))
                                                   (:streams data)))))
                           (:streams @app-state)))))

(defn load-saved-data []
  (when (not @loaded)
    (when-let [edn (aget js/localStorage local-storage-key)]
      (let [{:keys [ver state]} (cljs.reader/read-string edn)]
        (if (= ver storage-ver)
          (load state)
          (.removeItem js/localStorage local-storage-key))))))


(defn click-fn [& [f & args]]
  (fn [e]
    (.preventDefault e)
    (when f (apply f args))))

(defn checkbox-fn [path]
  (fn [e] (set-val! path (-> e .-target .-checked))))

(defn input-fn [path]
  (fn [e] (set-val! path (-> e .-target .-value))))

(defn update-component-meta [component m]
  (with-meta component
    (merge-with (fn [f1 f2] #(do (f1 %) (f2 %)))
                (meta component)
                m)))

(defn make-jq-widget [component widget-type opts]
  (let [widget-method (fn [this & args]
                         (let [widget (-> this r/dom-node js/$)]
                           (.apply (aget widget widget-type)
                                   widget
                                   (clj->js args))))]
    (update-component-meta
     component
     {:component-did-mount
      (fn [this] (widget-method this opts))
      :component-will-unmount
      (fn [this] (widget-method this "destroy"))})))

(defn make-jq-dialog [component opts]
  (let [visible (or (:visible opts) (atom true))
        opts (-> opts
                 (dissoc :visible)
                 (assoc :autoOpen @visible)
                 (assoc :close #(reset! visible false))
                 clj->js)
        dialog-method (fn [this arg]
                        (-> this r/dom-node js/$ (.dialog arg)))]
    (update-component-meta
     component
     {:component-did-mount
      (fn [this]
        (dialog-method this opts)
        (add-watch visible :vis-key
                   (fn [_ _ _ new-vis]
                     (dialog-method this (if new-vis "open" "close")))))
      :component-will-unmount
      (fn [this]
        (remove-watch visible :vis-key)
        (dialog-method this "destroy"))})))



(defn edit-new-stream []
  (set-val! [:edit :name] ""
            [:edit :is-editing] true))

(defn edit-stream [stream]
  (set-val! [:edit :stream-type] (:type stream)
            [:edit :name] (:name stream)
            [:edit :src] (:src stream)
            [:edit :is-editing] true))

(defn add-stream [stream]
  (update-val! :streams
               #(->> % (into [stream]) normalize-streams))
  (save))

(defn add-edited-stream []
  (let [{:keys [name stream-type src]} (:edit @app-state)]
    (add-stream {:name name
                 :type stream-type
                 :src  src
                 :visible true})
    (toggle! [:edit :is-editing])))

(defn reload-window [stream]
  (set-val! [:streams (:idx stream) :window] nil)
  (r/flush)
  (.setTimeout js/window
               #(set-val! [:streams (:idx stream) :window] (:window stream))))

(defonce win-start-pos (atom 0))

(defn new-window [stream]
  (swap! win-start-pos #(-> % (+ 50) (mod 400)))
  {:left @win-start-pos
   :top  @win-start-pos
   :width  (or (:width stream) 600)
   :height (or (:height stream) 345)
   :z-index 100})

(defn bring-to-front [stream]
  (let [prev-order (->> (:streams @app-state)
                        (filter :window)
                        (sort-by #(get-in % [:window :z-index])))
        new-order  (->> prev-order
                        (remove #(= % stream))
                        (#(concat % [stream]))
                        (map-indexed vector))]
    (when (not= stream (last prev-order))
      (doseq [[idx s] new-order]
        (set-val!
         [:streams (:idx s) :window :z-index]
         (+ 10 idx))))))

(defn toggle-window [stream]
  (let [path [:streams (:idx stream) :window]
        win (if (get-in @app-state path)
              nil
              (new-window stream))]
    (set-val! path win)
    (when win (bring-to-front stream))
    (save)))

(defn header []
  [:header
   [:nav
    [:a.streamLink.customizeLink
     {:href "#" :title "Customize"
      :class (when (:is-customizing @app-state) "active")
      :on-click (click-fn #(do
                             (toggle! :is-customizing)
                             (save)))}
     "_"]
    (for [stream (filter :visible (:streams @app-state))]
      ^{:key (:name stream)}
      [:a.streamLink {:href "#"
                      :class (when (:window stream) "active")
                      :on-click (click-fn toggle-window stream)}
       (:name stream)])]])


(defn jwplayer-component [stream]
  (update-component-meta
   (fn [] [:div])
   {:component-did-mount
    (fn [this]
      (let [node (r/dom-node this)
            div (.createElement js/document "div")
            args {:file (:src stream)
                  :title (or (:name stream) "Untitled")
                  :image (if (:image stream) "bomb.png")
                  :width "100%"
                  :aspectratio "16:9"
                  :rtmp {:subscribe true
                         :bufferlength (or (:bufferlength stream) 10)}}]
        (.setAttribute div "id" (str "player-" (gensym)))
        (.appendChild node div)
        (.setup (js/jwplayer div) (clj->js args))))

    :component-will-unmount
    (fn [this]
      (let [node (r/dom-node this)
            jwp-node (.querySelector node "object")]
        (try (.remove (js/jwplayer jwp-node))
          (catch :default e))))}))

(defn jwplayer-player [stream]
  (let [component (jwplayer-component stream)]
    (fn [] [component])))

(defn iframe-player [stream]
   [:iframe {:src (:src stream)
             :height "100%" :width "100%"
             :margin-height 0 :margin-width 0
             :frame-border 0 :scrolling "no"
             :allow-full-screen true}])

; {:dangerouslySetInnerHTML {:__html (:src stream)}}  --  doesn't evaluate scripts (jquery's .append does)

(defn html-player [stream]
  (let [component
          (with-meta
           (fn [] [:div])
           {:component-did-mount
            (fn [this]
             (-> this r/dom-node js/$ (.append (:src stream))))})]
    (fn [] [component])))

(defn player [stream]
  [:div.player
   (condp = (:type stream)
     :jwplayer [jwplayer-player stream]
     :iframe   [iframe-player stream]
     :html     [html-player stream])])

(defn window-content [stream]
  [:div.window {:style (:window stream)
                :on-mouse-down #(do
                                  (bring-to-front stream)
                                  (save))}
   [:div.title
    [:span (:name stream)]
    [:a.reloadBtn.ui-icon.ui-icon-arrowrefresh-1-e
     {:href "#" :title "Reload"
      :class (when-not (:show-reload @app-state) "hidden")
      :on-click (click-fn reload-window stream)}]]
   [:a.closeBtn.ui-icon.ui-icon-close
    {:href "#" :title "Close"
     :on-click (click-fn toggle-window stream)}]
   [player stream]
   [:div.overlay {:class (when-not (:show-overlays @app-state) "hidden")}]])

(defn window [stream]
  (let [path [:streams (:idx stream) :window]
        update-pos  (fn [pos]
                      (set-val! (conj path :top) (.-top pos)
                                (conj path :left) (.-left pos)))
        update-size (fn [size]
                      (set-val! (conj path :width) (.-width size)
                                (conj path :height) (.-height size)))
        win (-> window-content
                (make-jq-widget
                  "draggable"
                  {:cancel "object"
                   :start #(set-val! :show-overlays true)
                   :stop (fn [e ui]
                           (set-val! :show-overlays false)
                           (update-pos (.-position ui))
                           (save))})
                (make-jq-widget
                  "resizable"
                  {:handles "all"
                   :start #(set-val! :show-overlays true)
                   :stop (fn [e ui]
                           (set-val! :show-overlays false)
                           (update-pos (.-position ui))
                           (update-size (.-size ui))
                           (save))}))]
    (fn [stream]
      [win stream])))

(defn customize-dlg []
  [:div.customizeWindow {:class (if-not (:is-customizing @app-state) "hidden")}
   [:div.title "Customize"]
   [:a.ui-icon.ui-icon-close.closeBtn
    {:href "#" :title "Close"
     :on-click (click-fn #(do (set-val! :is-customizing false) (save)))}]
   [:a {:href "#" :on-click (click-fn edit-new-stream)} "Add Stream"]
   [:div.content
    (for [stream (:streams @app-state)]
      ^{:key (:name stream)}
      [:div
       [:input {:type "checkbox"
                :checked (:visible stream true)
                :on-change (checkbox-fn [:streams (:idx stream) :visible])}]
       [:span.name (:name stream)]
       [:a {:href "#" :on-click (click-fn edit-stream stream)} "Edit"]
       [:a.button.ui-icon.ui-icon-carat-1-n {:href "#" :on-click (click-fn)} " "]
       [:a.button.ui-icon.ui-icon-carat-1-s {:href "#" :on-click (click-fn)} " "]])
    [:hr]
    [:div
     [:label
      [:input {:type "checkbox" :checked (:show-reload @app-state)
               :on-change (checkbox-fn :show-reload)}]
      "Show reload buttons"]]]])


(defn edit-stream-dlg-content []
  [:div#editStreamDlg {:title (str "Add/Edit Stream")}
   [rf/bind-fields
    [:div
     [:p "Name: " [:input {:field :text :id :edit.name}]]
     [:p
      "Type: "
      [:label
       [:input {:field :radio :name :edit.stream-type :value :jwplayer}] "jwplayer"]
      [:label
       [:input {:field :radio :name :edit.stream-type :value :iframe}] "iframe"]
      [:label
       [:input {:field :radio :name :edit.stream-type :value :html}] "html"]]
     [:p "Source: " [:textarea {:field :textarea :id :edit.src}]]
     [:hr]
     [:button.ui-state-default
      {:on-click (click-fn add-edited-stream)} "OK"]
     [:button.ui-state-default
      {:on-click (click-fn #(toggle! [:edit :is-editing]))} "Cancel"]]
    app-state]])


(def edit-stream-dlg
  (make-jq-dialog
    edit-stream-dlg-content
    {:width 400
     :appendTo "#app"
     :visible (rc/cursor [:edit :is-editing] app-state)}))

(defn main []
  [:main
   [customize-dlg]
   (for [stream (:streams @app-state) :when (:window stream)]
     ^{:key (:name stream)}
     [window stream])])

(defn app []
  [:div#app
   [header]
   [main]
   [edit-stream-dlg]])

(defn init! []
  (aset js/jwplayer "key" "cH3LS/5ip1cRnTAeAfHTSnww0iWLW/Vb62KpZK+nusI=")
  (load-saved-data)
  (r/render [app] (.getElementById js/document "app-container")))

(enable-console-print!)

(defn print-state [state] (.log js/console (str state) "\n" (clj->js state)))
;(print-state (dissoc @app-state :streams))
;(add-watch app-state :key (fn [_ _ old-state new-state] (print-state new-state)))

(fw/start)
(init!)
