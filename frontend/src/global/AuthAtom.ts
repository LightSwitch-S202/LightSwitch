import { atom, selector } from 'recoil';
import { recoilPersist } from 'recoil-persist';

const { persistAtom } = recoilPersist();

interface MemberAtom {
  isAuthenticated: boolean;
  memberId: number;
  email: string;
  firstName: string;
  lastName: string;
}

export const AuthAtom = atom<MemberAtom>({
  key: 'AuthAtom',
  default: {
    isAuthenticated: false,
    memberId: 0,
    email: 'default',
    firstName: '',
    lastName: '',
  },
  effects_UNSTABLE: [persistAtom],
});

export const isLoggedInState = selector({
  key: 'isLoggedInState',
  get: ({ get }) => {
    const auth = get(AuthAtom);
    return auth.isAuthenticated;
  },
});
