var casper = require('casper').create({
  pageSettings: {
    loadImages:  true, 
  }
});

var fs = require('fs');

casper.userAgent('Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36');

casper.start();
casper.viewport(1024, 768);

casper.then(function() {
    this.open('http://s.weibo.com/weibo/%25E6%259D%25A5%25E8%2587%25AA%25E6%2598%259F%25E6%2598%259F%25E7%259A%2584%25E4%25BD%25A0?topnav=1&wvr=5&b=1').then(function(){
      this.page.switchToChildFrame(0); 
      fs.write("1.html", this.getPageContent(), 'w');  

      this.page.switchToParentFrame();
      fs.write("2.html", this.getPageContent(), 'w');
    });
});

casper.run();