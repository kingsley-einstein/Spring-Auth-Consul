package com.spring.consul.hooks;

import java.io.Serializable;

import com.spring.consul.model.Auth;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class AuthListener extends EmptyInterceptor {
  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (entity instanceof Auth) {
      for (int i = 0; i < propertyNames.length; i++) {
        if (propertyNames[i].equals("password")) {
          state[i] = encoder.encode((String) state[i]);
          return true;
        }
      }
    }
    return super.onSave(entity, id, state, propertyNames, types);
  }

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
      String[] propertyNames, Type[] types) {
    if (entity instanceof Auth) {
      for (int i = 0; i < propertyNames.length; i++) {
        if (propertyNames[i].equals("password")) {
          currentState[i] = encoder.encode((String) currentState[i]);
          return true;
        }
      }
    }
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }
}
