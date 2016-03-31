function Promise(fn) {
    var state = 'pending';
    var value;
    var deferred = null ;
    
    function resolve(newValue) {
        if (newValue && typeof newValue.then === 'function') {
            newValue.then(resolve);
            return;
        }
        
        value = newValue;
        state = 'resolved';
        
        if (deferred) {
            handle(deferred);
        }
    }
    
    function handle(handler) {
        if (state === 'pending') {
            deferred = handler;
            return;
        }
        
        if (!handler.onResolved) {
            console.log('------------------');
            handler.resolve(value);
            return;
        }
        
        var ret = handler.onResolved(value);
        handler.resolve(ret);
    }
    
    this.then = function(onResolved) {
        return new Promise(function(resolve) {
            //console.log('------------------');
            //console.log(onResolved);
            //console.log(resolve);
            handle({
                onResolved: onResolved,
                resolve: resolve
            });
        }
        )
    }
    
    fn(resolve);
}

function doSomething() {
    return new Promise((resolve)=>resolve(42));
}

doSomething().then(function(value) {
    return new Promise((resolve)=>resolve(value+1));
}).then(function(value) {
    console.log("the final result is", value);
    return value;
}).then(value=>value+1);
