# How React Rxjs works

1. An observable must have an active subscription before its hook can be executed. (TODO: What happens when this is not the case?)

2. A component subscribing to an observable is suspended until the observable emits a value that can be used as state by the component (using the observable's hook).

`Subscribe` performs two jobs:

1. can be used as a point to subscribe to an observable before its hook is used in one of its child components.

2. can hold subscriptions to observables used by its child components, which in effect makes these subscriptions to these observables "persistent" across changes (add, remove) in child components. For example, if a user navigates back to previous screen, if the navigation tree is wrapped into a `Subscribe` component, then the values on the previous screen are retained. Without `Subscribe` in such a situation, the state held by these underlying observables is cleaned up as soon as the components that hold the subscriptions locally are unmounted.

An alternative to the 1st point is to provide a default values to `bind()`. However, `Subscribe` won't perform its first job - automatically subscribe to a subscription in a child component - if an observable already has a default value (because there is no need to). But, as a side-effect, now it can also not perform its 2nd job - hold a subscription to such an observable with a default value. So, as soon as a component which subscribes to this observable is unmounted, the associated state is lost.

## Accessing an observable value in a React component`bind` and `Subscribe`

To access an observable in a React component, `bind` gives you a hook